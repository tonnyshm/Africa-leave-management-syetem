package com.africa.hr.leave.leave_service.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestDto {
    private Long leaveTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String department;
}

