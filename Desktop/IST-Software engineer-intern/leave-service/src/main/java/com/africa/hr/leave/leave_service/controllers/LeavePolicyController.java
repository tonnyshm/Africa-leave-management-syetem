package com.africa.hr.leave.leave_service.controllers;

import com.africa.hr.leave.leave_service.models.LeavePolicy;
import com.africa.hr.leave.leave_service.services.LeavePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/manage")
@PreAuthorize("hasRole('ADMIN')")
public class LeavePolicyController {

    @Autowired
    private LeavePolicyService leavePolicyService;

    // --- Leave Policy ---
    @GetMapping("/policies")
    public List<LeavePolicy> getPolicies() {
        return leavePolicyService.getAllPolicies();
    }

    @PostMapping("/policies")
    public LeavePolicy createPolicy(@RequestBody LeavePolicy policy) {
        return leavePolicyService.createPolicy(policy);
    }

    @PutMapping("/policies/{id}")
    public LeavePolicy updatePolicy(@PathVariable Long id, @RequestBody LeavePolicy updated) {
        return leavePolicyService.updatePolicy(id, updated);
    }

    @DeleteMapping("/policies/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        leavePolicyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }
}
