package com.africa.hr.auth.auth_service.services;

import com.africa.hr.auth.auth_service.models.Role;
import com.africa.hr.auth.auth_service.models.User;
import com.africa.hr.auth.auth_service.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleCheckService {

    private final UserRepository userRepository;

    public RoleCheckService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isManagerOrAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return user.getRole() == Role.MANAGER || user.getRole() == Role.ADMIN;
    }
}


