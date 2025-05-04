package com.africa.hr.leave.leave_service.services;

import com.africa.hr.leave.leave_service.models.LeavePolicy;
import com.africa.hr.leave.leave_service.repositories.LeavePolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeavePolicyService {

    private final LeavePolicyRepository leavePolicyRepository;

    @Autowired
    public LeavePolicyService(LeavePolicyRepository leavePolicyRepository) {
        this.leavePolicyRepository = leavePolicyRepository;
    }

    public List<LeavePolicy> getAllPolicies() {
        return leavePolicyRepository.findAll();
    }

    public LeavePolicy createPolicy(LeavePolicy policy) {
        return leavePolicyRepository.save(policy);
    }

    public LeavePolicy updatePolicy(Long id, LeavePolicy updated) {
        LeavePolicy existing = leavePolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        existing.setMonthlyAccrualRate(updated.getMonthlyAccrualRate());
        existing.setMaxCarryover(updated.getMaxCarryover());

        return leavePolicyRepository.save(existing);
    }

    public void deletePolicy(Long id) {
        leavePolicyRepository.deleteById(id);
    }
}

