package com.africa.hr.leave.leave_service.repositories;


import com.africa.hr.leave.leave_service.models.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findByUserEmail(String userEmail);

}
