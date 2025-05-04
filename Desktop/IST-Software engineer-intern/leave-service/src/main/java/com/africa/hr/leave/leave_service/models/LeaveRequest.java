package com.africa.hr.leave.leave_service.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail; // Associate leave with user's email (we get it from auth service)

    @ManyToOne
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;


    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    private String attachmentUrl; // Optional supporting document

    @Enumerated(EnumType.STRING)
    private LeaveStatus status; // PENDING, APPROVED, REJECTED

    private String department;

    private LocalDateTime createdAt;


}

