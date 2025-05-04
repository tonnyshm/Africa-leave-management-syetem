package com.africa.hr.leave.leave_service.repositories;

import com.africa.hr.leave.leave_service.models.LeaveType;
import com.africa.hr.leave.leave_service.models.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByUserEmail(String userEmail);

    // 🔄 Updated from leaveType to leavePolicy
    List<LeaveRequest> findByLeaveType(LeaveType leaveType);

    List<LeaveRequest> findByUserEmailAndLeaveType(String email, LeaveType leaveType);

    @Query("SELECT l FROM LeaveRequest l WHERE l.status = 'APPROVED' AND :now BETWEEN l.startDate AND l.endDate")
    List<LeaveRequest> findActiveApprovedLeaves(@Param("now") LocalDate now);

    @Query("SELECT l FROM LeaveRequest l WHERE l.status = 'APPROVED' AND :now BETWEEN l.startDate AND l.endDate AND l.department = :dept")
    List<LeaveRequest> findActiveApprovedLeavesByDepartment(@Param("now") LocalDate now, @Param("dept") String dept);

    List<LeaveRequest> findByUserEmailAndLeaveTypeAndDepartment(String email, LeaveType leaveType, String department);

    List<LeaveRequest> findByUserEmailAndDepartment(String email, String department);

    List<LeaveRequest> findByLeaveTypeAndDepartment(LeaveType leaveType, String department);

    List<LeaveRequest> findByDepartment(String department);


}
