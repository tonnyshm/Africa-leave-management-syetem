package com.africa.hr.leave.leave_service.repositories;

import com.africa.hr.leave.leave_service.models.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {

    boolean existsByType(String type);

    LeaveType findByType(String type);
}

