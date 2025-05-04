package com.africa.hr.leave.leave_service.services;

import com.africa.hr.leave.leave_service.models.LeaveType;
import com.africa.hr.leave.leave_service.repositories.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    @Autowired
    public LeaveTypeService(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
    }

    public List<LeaveType> getAllType() {
        return leaveTypeRepository.findAll();
    }

    public LeaveType createType(LeaveType type) {
        if (leaveTypeRepository.existsByType(type.getType())) {
            throw new RuntimeException("Leave type already exists: " + type.getType());
        }
        return leaveTypeRepository.save(type);
    }

    public void deleteType(Long id) {
        if (!leaveTypeRepository.existsById(id)) {
            throw new RuntimeException("Leave policy not found with id: " + id);
        }
        leaveTypeRepository.deleteById(id);
    }

    public LeaveType updateType(Long id, LeaveType updatedType) {
        LeaveType existing = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found with id: " + id));

        existing.setType(updatedType.getType());
        return leaveTypeRepository.save(existing);
    }

    public LeaveType getTypeByType(String type) {
        return leaveTypeRepository.findByType(type);
    }
}
