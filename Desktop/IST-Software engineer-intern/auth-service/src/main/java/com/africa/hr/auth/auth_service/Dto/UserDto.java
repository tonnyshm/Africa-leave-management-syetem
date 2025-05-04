package com.africa.hr.auth.auth_service.Dto;

import com.africa.hr.auth.auth_service.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String email;
    private String profilePictureUrl;
    private String fullName;
    private String department;
    private String role;



    public static UserDto fromEntity(User user) {
        return new UserDto(user.getEmail(), user.getProfilePictureUrl(),user.getFullName(),user.getDepartment(), user.getRole().name());
    }
}
