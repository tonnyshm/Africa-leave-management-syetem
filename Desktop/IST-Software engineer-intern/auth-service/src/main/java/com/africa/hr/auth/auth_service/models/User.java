package com.africa.hr.auth.auth_service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String fullName;

    private String profilePictureUrl;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STAFF; // Default role STAFF

    @Column
    private String department;


    private LocalDateTime createdAt;
}
