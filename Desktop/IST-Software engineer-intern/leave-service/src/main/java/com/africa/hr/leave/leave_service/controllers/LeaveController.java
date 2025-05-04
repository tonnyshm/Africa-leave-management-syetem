package com.africa.hr.leave.leave_service.controllers;

import com.africa.hr.leave.leave_service.models.*;
import com.africa.hr.leave.leave_service.repositories.LeaveRepository;
import com.africa.hr.leave.leave_service.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    private final AuthServiceClient authServiceClient;

    @Autowired
    private CloudinaryService cloudinaryService;


    public LeaveController(LeaveService leaveService, AuthServiceClient authServiceClient) {
        this.leaveService = leaveService;
        this.authServiceClient = authServiceClient;
    }

    @PostMapping(value = "/apply", consumes = {"multipart/form-data"})
    public ResponseEntity<LeaveRequest> applyLeave(
            @RequestPart("leaveRequest") String leaveRequestJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        LeaveRequestDto leaveRequestDto;
        try {
            leaveRequestDto = mapper.readValue(leaveRequestJson, LeaveRequestDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse leaveRequest JSON", e);
        }

        String fileUrl = null;
        try {
            if (file != null && !file.isEmpty()) {
                fileUrl = cloudinaryService.uploadFile(file);
            }
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }

        LeaveRequest saved = leaveService.applyLeave(leaveRequestDto, fileUrl);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/user/{email}")
    public List<LeaveRequest> getUserLeaves(@PathVariable String email) {
        return leaveService.getUserLeaves(email);
    }

    @GetMapping
    public List<LeaveRequest> getAllLeaves() {
        return leaveService.getAllLeaves();
    }



    @PostMapping("/{leaveId}/approve")
    public LeaveRequest approveLeave(@PathVariable Long leaveId, Principal principal) {
        String email = ((Jwt) ((Authentication) principal).getPrincipal()).getClaimAsString("email");

        Role role = authServiceClient.getUserRole(email);

        if (role != Role.MANAGER && role != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to approve leaves.");
        }

        return leaveService.approveLeave(leaveId);
    }


    @PostMapping("/{leaveId}/reject")
    public LeaveRequest rejectLeave(@PathVariable Long leaveId, @RequestBody RejectLeaveRequest rejectRequest, Principal principal) {
        String email = ((Jwt) ((Authentication) principal).getPrincipal()).getClaimAsString("email");

        Role role = authServiceClient.getUserRole(email);

        if (role != Role.MANAGER && role != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to reject leaves.");
        }

        return leaveService.rejectLeave(leaveId, rejectRequest.getComment());
    }


    // 1. Current user's leave history
    @GetMapping("/my-history")
    public ResponseEntity<List<LeaveRequest>> getMyLeaveHistory(Principal principal) {
        String email = ((Jwt) ((Authentication) principal).getPrincipal()).getClaimAsString("email");
        List<LeaveRequest> leaves = leaveRepository.findByUserEmail(email);
        return ResponseEntity.ok(leaves);
    }



    // 2. Team leave calendar (all leaves for MANAGERs and ADMINs)
    @GetMapping("/team-on-leave-for-admin")
    public ResponseEntity<List<LeaveRequest>> getTeamOnLeave(Principal principal) {
        String email = ((Jwt) ((Authentication) principal).getPrincipal()).getClaimAsString("email");
        Role role = authServiceClient.getUserRole(email);

        if (role != Role.MANAGER && role != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<LeaveRequest> leaves = leaveRepository.findAll();
        return ResponseEntity.ok(leaves);
    }



    // 3. Leave balance (already implemented?)
    @GetMapping("/balance")
    public ResponseEntity<?> getLeaveBalance(Principal principal) {
        String email = ((Jwt) ((Authentication) principal).getPrincipal()).getClaimAsString("email");
        double balance = leaveBalanceService.getBalance(email).getPtoBalance();
        return ResponseEntity.ok(Map.of("days", balance));
    }

    @GetMapping("/team-on-leave")
    public ResponseEntity<List<TeamLeaveDto>> getTeamOnLeave(@RequestParam(required = false) String department, Principal principal) {
        String viewerEmail = principal.getName(); // Authenticated user

        // Fetch from leaveService
        List<TeamLeaveDto> result = leaveService.getTeamOnLeave(viewerEmail, department);
        return ResponseEntity.ok(result);
    }




}

