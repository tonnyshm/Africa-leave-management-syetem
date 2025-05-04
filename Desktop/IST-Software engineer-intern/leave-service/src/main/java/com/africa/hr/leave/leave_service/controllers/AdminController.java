package com.africa.hr.leave.leave_service.controllers;

import com.africa.hr.leave.leave_service.models.LeaveBalance;
import com.africa.hr.leave.leave_service.models.LeaveRequest;
import com.africa.hr.leave.leave_service.repositories.LeaveRepository;
import com.africa.hr.leave.leave_service.services.AuthServiceClient;
import com.africa.hr.leave.leave_service.services.LeaveBalanceService;
import com.africa.hr.leave.leave_service.services.LeaveService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leaves/admin")
@PreAuthorize("hasRole('ADMIN')")  // Applies to all endpoints in this controller
public class AdminController {

    private final AuthServiceClient authServiceClient;


    @Autowired
    private LeaveBalanceService leaveBalanceService;

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private LeaveService leaveService;


    public AdminController(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @GetMapping("/users")
    public List<Map<String, Object>> listAllUsers() {
        return authServiceClient.getAllUsers();
    }

    @PostMapping("/set-org-balance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> setOrgWidePtoBalance(@RequestParam double balance) {
        leaveBalanceService.setOrgWideBalance(balance);
        return ResponseEntity.ok("All user PTO balances have been updated to " + balance + " days.");
    }


    // ✅ Adjust Leave Balance
    @PatchMapping("/users/{email}/balance")
    public ResponseEntity<?> adjustUserBalance(
            @PathVariable String email,
            @RequestParam double amount
    ) {
        LeaveBalance updated = leaveBalanceService.adjustBalanceByHr(email, amount);
        return ResponseEntity.ok(Map.of(
                "message", "Balance updated successfully",
                "email", updated.getUserEmail(),
                "newBalance", updated.getPtoBalance()
        ));
    }

    @GetMapping("/users/{email}/leaves")
    @PreAuthorize("hasRole('ADMIN')")
    public List<LeaveRequest> getUserLeaveHistory(@PathVariable String email) {
        return leaveService.getUserLeaves(email);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reports")
    public List<LeaveRequest> generateReport(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long leaveTypeId,
            @RequestParam(required = false) String department
    ) {
        return leaveService.filterLeaves(email, leaveTypeId, department);
    }



    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=leave-report.csv");

        List<LeaveRequest> leaves = leaveRepository.findAll();
        PrintWriter writer = response.getWriter();
        writer.println("Email,LeaveType,StartDate,EndDate,Status");

        for (LeaveRequest leave : leaves) {
            writer.printf("%s,%s,%s,%s,%s%n",
                    leave.getUserEmail(),
                    leave.getLeaveType().getType(),
                    leave.getStartDate(),
                    leave.getEndDate(),
                    leave.getStatus());
        }
        writer.flush();
        writer.close();
    }


//    @GetMapping(value = "/reports/export", produces = "text/csv")
@GetMapping("/reports/export")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<String> exportLeaveReportsToCSV(
        @RequestParam(required = false) String email,
        @RequestParam(required = false) Long leaveTypeId,
        @RequestParam(required = false) String department
) {
    // Normalize blank inputs
    if (email != null && email.trim().isEmpty()) email = null;
    if (department != null && department.trim().isEmpty()) department = null;

    // Get filtered leaves based on new dynamic policy
    List<LeaveRequest> leaves = leaveService.filterLeaves(email, leaveTypeId, department);

    // Build CSV content
    StringWriter writer = new StringWriter();
    writer.append("ID,User Email,Leave Type,Start Date,End Date,Status,Reason,Created At\n");

    for (LeaveRequest leave : leaves) {
        writer.append(leave.getId() != null ? leave.getId().toString() : "")
                .append(',')
                .append(escape(leave.getUserEmail()))
                .append(',')
                .append(escape(leave.getLeaveType() != null ? leave.getLeaveType().getType() : ""))
                .append(',')
                .append(leave.getStartDate().toString())
                .append(',')
                .append(leave.getEndDate().toString())
                .append(',')
                .append(leave.getStatus().name())
                .append(',')
                .append(escape(leave.getReason()))
                .append(',')
                .append(leave.getCreatedAt().toString())
                .append('\n');
    }

    return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=leave-report.csv")
            .header("Content-Type", "text/csv")
            .body(writer.toString());
}

    private String escape(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }



}

