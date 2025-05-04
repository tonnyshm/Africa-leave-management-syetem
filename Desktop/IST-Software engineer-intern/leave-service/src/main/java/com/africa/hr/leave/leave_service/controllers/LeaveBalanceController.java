package com.africa.hr.leave.leave_service.controllers;



import com.africa.hr.leave.leave_service.services.LeaveBalanceService;
import com.africa.hr.leave.leave_service.models.LeaveBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balance")
public class LeaveBalanceController {

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    @GetMapping("/{email}")
    public LeaveBalance getBalance(@PathVariable String email) {
        return leaveBalanceService.getBalance(email);
    }

    @PostMapping("/adjust")
    public LeaveBalance adjustBalance(@RequestParam String email, @RequestParam double days) {
        return leaveBalanceService.adjustBalance(email, days);
    }
}

