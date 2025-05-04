package com.africa.hr.leave.leave_service.models;

// TeamLeaveDto.java
import lombok.Data;

import java.time.LocalDate;

@Data
public class TeamLeaveDto {
    private String fullName;
    private String email;
    private String profilePictureUrl;
    private String department;
    private LocalDate endDate;
}

