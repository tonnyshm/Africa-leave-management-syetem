package com.africa.hr.leave.leave_service.services;


import com.africa.hr.leave.leave_service.models.*;
import com.africa.hr.leave.leave_service.repositories.LeaveTypeRepository;
import com.africa.hr.leave.leave_service.repositories.LeaveRepository;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Objects;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;
    @Autowired
    private LeaveBalanceService leaveBalanceService;

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private EmailService emailService;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    public LeaveRequest applyLeave(LeaveRequestDto dto, String fileUrl) {
        long daysRequested = dto.getEndDate().toEpochDay() - dto.getStartDate().toEpochDay() + 1;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String email = jwt.getClaimAsString("email");

        LeaveType type = leaveTypeRepository.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Invalid leave policy"));

        LeaveBalance balance = leaveBalanceService.getBalance(email);
        if (balance.getPtoBalance() < daysRequested) {
            throw new RuntimeException("Insufficient leave balance");
        }

        leaveBalanceService.deductBalance(email, daysRequested);

        LeaveRequest request = LeaveRequest.builder()
                .userEmail(email)
                .leaveType(type)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .reason(dto.getReason())
                .department(dto.getDepartment())
                .status(LeaveStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .attachmentUrl(fileUrl)
                .build();

        return leaveRepository.save(request);
    }



    public List<LeaveRequest> getUserLeaves(String email) {
        return leaveRepository.findByUserEmail(email);
    }

    public List<LeaveRequest> getAllLeaves() {
        return leaveRepository.findAll();
    }


    public LeaveRequest approveLeave(Long leaveId) {
        Optional<LeaveRequest> leaveOpt = leaveRepository.findById(leaveId);
        if (leaveOpt.isPresent()) {
            LeaveRequest leave = leaveOpt.get();
            leave.setStatus(LeaveStatus.APPROVED);
            LeaveRequest saved = leaveRepository.save(leave);

            // 🔔 Notify user
            emailService.sendLeaveApprovedNotification(
                    saved.getUserEmail(),
                    saved.getStartDate().toString(),
                    saved.getEndDate().toString()
            );

            return saved;
        }
        throw new RuntimeException("Leave not found");
    }


    public LeaveRequest rejectLeave(Long leaveId, String comment) {
        Optional<LeaveRequest> leaveOpt = leaveRepository.findById(leaveId);
        if (leaveOpt.isPresent()) {
            LeaveRequest leave = leaveOpt.get();
            leave.setStatus(LeaveStatus.REJECTED);
            leave.setReason(comment); // Save the rejection reason as comment
            leaveRepository.save(leave);

            // Optionally: refund the leave balance if rejected
            long days = leave.getEndDate().toEpochDay() - leave.getStartDate().toEpochDay() + 1;
            leaveBalanceService.adjustBalance(leave.getUserEmail(), days); // Refund days

            // 🔔 Notify user
            emailService.sendLeaveRejectedNotification(
                    leave.getUserEmail(),
                    comment
            );

            return leave;
        }
        throw new RuntimeException("Leave not found");
    }


    public List<LeaveRequest> filterLeaves(String email, Long LeaveTypeId, String department) {
        LeaveType type = null;
        if (LeaveTypeId != null) {
            type = leaveTypeRepository.findById(LeaveTypeId)
                    .orElseThrow(() -> new RuntimeException("Leave Type not found"));
        }

        if (email != null && type != null && department != null) {
            return leaveRepository.findByUserEmailAndLeaveTypeAndDepartment(email, type, department);
        } else if (email != null && type != null) {
            return leaveRepository.findByUserEmailAndLeaveType(email, type);
        } else if (email != null && department != null) {
            return leaveRepository.findByUserEmailAndDepartment(email, department);
        } else if (type != null && department != null) {
            return leaveRepository.findByLeaveTypeAndDepartment(type, department);
        } else if (email != null) {
            return leaveRepository.findByUserEmail(email);
        } else if (type != null) {
            return leaveRepository.findByLeaveType(type);
        } else if (department != null) {
            return leaveRepository.findByDepartment(department);
        } else {
            return leaveRepository.findAll();
        }
    }


    public List<TeamLeaveDto> getTeamOnLeave(String requesterEmail, String department) {
        // Fetch all APPROVED leaves currently active
        List<LeaveRequest> leaves = (department == null) ?
                leaveRepository.findActiveApprovedLeaves(LocalDate.now()) :
                leaveRepository.findActiveApprovedLeavesByDepartment(LocalDate.now(), department);

        // Filter out the current user (viewer)
        leaves = leaves.stream()
                .filter(lr -> !lr.getUserEmail().equalsIgnoreCase(requesterEmail))
                .collect(Collectors.toList());

        // Fetch user details from auth-service
        List<String> emails = leaves.stream().map(LeaveRequest::getUserEmail).distinct().toList();
        List<UserDto> users = authServiceClient.getUsersByEmails(emails);

        // Merge info
        return leaves.stream().map(leave -> {
            UserDto user = users.stream()
                    .filter(u -> u.getEmail().equalsIgnoreCase(leave.getUserEmail()))
                    .findFirst().orElse(null);

            if (user == null) return null;

            TeamLeaveDto dto = new TeamLeaveDto();
            dto.setEmail(user.getEmail());
            dto.setFullName(user.getFullName());
            dto.setProfilePictureUrl(user.getProfilePictureUrl());
            dto.setDepartment(user.getDepartment());
            dto.setEndDate(leave.getEndDate());
            return dto;
        }).filter(Objects::nonNull).toList();
    }

}

