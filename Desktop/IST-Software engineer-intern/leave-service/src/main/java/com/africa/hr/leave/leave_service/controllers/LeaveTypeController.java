package com.africa.hr.leave.leave_service.controllers;

import com.africa.hr.leave.leave_service.models.LeaveType;
import com.africa.hr.leave.leave_service.services.LeaveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves/types")
@PreAuthorize("hasRole('ADMIN')") // Only admin can access this controller
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @Autowired
    public LeaveTypeController(LeaveTypeService leaveTypeService) {
        this.leaveTypeService = leaveTypeService;
    }

    // ✅ Get all types
    @GetMapping("/list")
    public ResponseEntity<List<LeaveType>> getAllPolicies() {
        return ResponseEntity.ok(leaveTypeService.getAllType());
    }

    // ✅ Create a new leave type
    @PostMapping
    public ResponseEntity<LeaveType> createPolicy(@RequestBody LeaveType type) {
        LeaveType created = leaveTypeService.createType(type);
        return ResponseEntity.ok(created);
    }

    // ✅ Update existing type by ID
    @PutMapping("/{id}")
    public ResponseEntity<LeaveType> updatePolicy(@PathVariable Long id, @RequestBody LeaveType updatedtype) {
        LeaveType updated = leaveTypeService.updateType(id, updatedtype);
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete a type by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteType(@PathVariable Long id) {
        leaveTypeService.deleteType(id);
        return ResponseEntity.ok().build();
    }

    // ✅ Optional: Get types by types
    @GetMapping("/type/{type}")
    public ResponseEntity<LeaveType> getTypeByType(@PathVariable String type) {
        LeaveType types = leaveTypeService.getTypeByType(type);
        return ResponseEntity.ok(types);
    }
}
