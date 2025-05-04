package com.africa.hr.leave.leave_service.repositories;

import com.africa.hr.leave.leave_service.models.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, Long> {

        Optional<LeavePolicy> findTopByOrderByIdAsc();

}
