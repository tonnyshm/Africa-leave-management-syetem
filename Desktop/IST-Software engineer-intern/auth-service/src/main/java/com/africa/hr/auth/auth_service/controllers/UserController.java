package com.africa.hr.auth.auth_service.controllers;

import com.africa.hr.auth.auth_service.Dto.UserDto;
import com.africa.hr.auth.auth_service.models.Role;
import com.africa.hr.auth.auth_service.models.User;
import com.africa.hr.auth.auth_service.repositories.UserRepository;
import com.africa.hr.auth.auth_service.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")   // Expose User Role in Auth-Service ... Exposes: GET /api/users/{email}/role
// Response will be simple like: "MANAGER" , "STAFF" or "ADMIN"
public class UserController {

    private final UserRepository userRepository;

    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/{email}/role")
    public ResponseEntity<Role> getUserRole(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return ResponseEntity.ok(user.getRole());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String email = jwt.getClaimAsString("email");
        String fullName = jwt.getClaimAsString("name");
        String profilePictureUrl = jwt.getClaimAsString("picture");

        // ✅ Create user if not exist
        User user = userService.findOrCreateUser(email, fullName, profilePictureUrl);

        return ResponseEntity.ok(Map.of(
                "email", user.getEmail(),
                "fullName", user.getFullName(),
                "profilePictureUrl", user.getProfilePictureUrl(),
                "role", user.getRole().name(),
                "department", user.getDepartment()
        ));
    }

    @PutMapping("/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> promoteUser(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newRole = request.get("role"); // "ADMIN" or "MANAGER"

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(Role.valueOf(newRole));
        userRepository.save(user);

        return ResponseEntity.ok("User promoted to " + newRole);
    }

    @GetMapping("/list_users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> listAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getEmail(), user.getProfilePictureUrl(),user.getFullName(), user.getDepartment(), user.getRole().name()))
                .collect(Collectors.toList());
    }
    @PostMapping("/batch")
    public ResponseEntity<List<UserDto>> getUsersByEmails(@RequestBody List<String> emails) {
        List<User> users = userRepository.findByEmailIn(emails);

        List<UserDto> dtos = users.stream().map(user -> {
            UserDto dto = new UserDto();
            dto.setEmail(user.getEmail());
            dto.setFullName(user.getFullName());
            dto.setProfilePictureUrl(user.getProfilePictureUrl());
            dto.setRole(user.getRole().name());
            dto.setDepartment(user.getDepartment());
            return dto;
        }).toList();

        return ResponseEntity.ok(dtos);
    }



}