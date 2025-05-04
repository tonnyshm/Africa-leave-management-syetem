package com.africa.hr.leave.leave_service.services;

import com.africa.hr.leave.leave_service.models.LeaveBalance;
import com.africa.hr.leave.leave_service.repositories.LeaveBalanceRepository;
import com.africa.hr.leave.leave_service.repositories.LeavePolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.africa.hr.leave.leave_service.models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveBalanceService {

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private LeavePolicyRepository leavePolicyRepository;

    // Default fallback values
    private static final double DEFAULT_MONTHLY_ACCRUAL_RATE = 1.66;
    private static final double DEFAULT_MAX_CARRYOVER_DAYS = 5.0;

    public double getMonthlyAccrualRate() {
        return leavePolicyRepository.findTopByOrderByIdAsc()
                .map(LeavePolicy::getMonthlyAccrualRate)
                .orElse(DEFAULT_MONTHLY_ACCRUAL_RATE);
    }

    public double getMaxCarryoverDays() {
        return leavePolicyRepository.findTopByOrderByIdAsc()
                .map(LeavePolicy::getMaxCarryover)
                .orElse(DEFAULT_MAX_CARRYOVER_DAYS);
    }

    public LeaveBalance createBalanceIfNotExist(String email) {
        Optional<LeaveBalance> existing = leaveBalanceRepository.findByUserEmail(email);
        if (existing.isPresent()) {
            return existing.get();
        }
        LeaveBalance newBalance = LeaveBalance.builder()
                .userEmail(email)
                .ptoBalance(0.0)
                .lastAccrualUpdate(LocalDateTime.now())
                .build();
        return leaveBalanceRepository.save(newBalance);
    }

    public LeaveBalance getBalance(String email) {
        return leaveBalanceRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Balance not found for user: " + email));
    }

    public void setOrgWideBalance(double newBalance) {
        List<LeaveBalance> allBalances = leaveBalanceRepository.findAll();

        for (LeaveBalance balance : allBalances) {
            balance.setPtoBalance(newBalance);
            balance.setLastAccrualUpdate(LocalDateTime.now());
        }

        leaveBalanceRepository.saveAll(allBalances);
    }

    public LeaveBalance adjustBalance(String email, double days) {
        LeaveBalance balance = leaveBalanceRepository.findByUserEmail(email)
                .orElseGet(() -> LeaveBalance.builder()
                        .userEmail(email)
                        .ptoBalance(0.0)
                        .lastAccrualUpdate(LocalDateTime.now())
                        .build()
                );

        balance.setPtoBalance(balance.getPtoBalance() + days);
        return leaveBalanceRepository.save(balance);
    }

    public LeaveBalance adjustBalanceByHr(String email, double days) {
        LeaveBalance balance = leaveBalanceRepository.findByUserEmail(email)
                .orElseGet(() -> LeaveBalance.builder()
                        .userEmail(email)
                        .ptoBalance(0.0)
                        .lastAccrualUpdate(LocalDateTime.now())
                        .build()
                );

        balance.setPtoBalance(days);
        return leaveBalanceRepository.save(balance);
    }


    public void deductBalance(String email, double days) {
        LeaveBalance balance = getBalance(email);
        if (balance.getPtoBalance() >= days) {
            balance.setPtoBalance(balance.getPtoBalance() - days);
            leaveBalanceRepository.save(balance);
        } else {
            throw new RuntimeException("Insufficient leave balance");
        }
    }

    // Scheduled method to accrue 1.66 days monthly for all users
    @Scheduled(cron = "0 0 0 1 * *") // Every 1st of the month at midnight
    public void accrueMonthlyLeave() {
        List<LeaveBalance> balances = leaveBalanceRepository.findAll();
        for (LeaveBalance balance : balances) {
            balance.setPtoBalance(balance.getPtoBalance() + getMonthlyAccrualRate());
            balance.setLastAccrualUpdate(LocalDateTime.now());
            leaveBalanceRepository.save(balance);
        }
    }

    // Optional: Handle carryover limit manually if needed
    public void enforceCarryoverLimit() {
        List<LeaveBalance> balances = leaveBalanceRepository.findAll();
        for (LeaveBalance balance : balances) {
            if (balance.getPtoBalance() > 20 + getMaxCarryoverDays()) {
                balance.setPtoBalance(20 + getMaxCarryoverDays()); // Reset to max allowed
                leaveBalanceRepository.save(balance);
            }
        }
    }

    // Run once per year - Reset carryover balance
    // Runs automatically at Jan 1st, midnight (Kigali time uses UTC+2, but for now simple cron is enough — but we can specify timezone if needed).
    //Checks all users.
    //Resets their PTO balance to 5 days max if it exceeds.
    @Scheduled(cron = "0 0 0 1 1 *") // 1st January every year at midnight
    public void enforceCarryoverPolicy() {
        List<LeaveBalance> balances = leaveBalanceRepository.findAll();
        for (LeaveBalance balance : balances) {
            if (balance.getPtoBalance() > 5.0) {
                balance.setPtoBalance(5.0); // Only allow 5 days carryover
                leaveBalanceRepository.save(balance);
            }
        }
    }

}

