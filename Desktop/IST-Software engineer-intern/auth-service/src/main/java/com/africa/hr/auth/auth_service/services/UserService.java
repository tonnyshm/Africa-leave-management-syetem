package com.africa.hr.auth.auth_service.services;

import com.africa.hr.auth.auth_service.Dto.UserDto;
import com.africa.hr.auth.auth_service.models.Role;
import com.africa.hr.auth.auth_service.models.User;
import com.africa.hr.auth.auth_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findOrCreateUser(String email, String fullName, String profilePictureUrl) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) return existingUser.get();

        Role role = userRepository.count() == 0 ? Role.ADMIN : Role.STAFF;

        User newUser = User.builder()
                .email(email)
                .fullName(fullName)
                .profilePictureUrl(profilePictureUrl)
                .role(role)
                .department("General")
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(newUser);
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null); // or throw new RuntimeException("User not found");
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(user -> new UserDto(
                user.getEmail(),
                user.getFullName(),
                user.getProfilePictureUrl(),
                user.getDepartment(),
                user.getRole().name()
        )).collect(Collectors.toList());
    }



}
