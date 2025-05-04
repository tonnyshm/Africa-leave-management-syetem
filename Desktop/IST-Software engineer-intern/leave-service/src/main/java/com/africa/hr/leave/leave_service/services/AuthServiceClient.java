package com.africa.hr.leave.leave_service.services;


import com.africa.hr.leave.leave_service.models.Role;
import com.africa.hr.leave.leave_service.models.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AuthServiceClient {

    private final RestTemplate restTemplate;


    @Value("${auth.service.url}")
    private String authServiceUrl; // Example: http://localhost:8081

    public AuthServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Role getUserRole(String email) {
        String url = authServiceUrl + "/api/users/" + email + "/role";
        return restTemplate.getForObject(url, Role.class);
    }

    // ✅ New: Fetch all users for Admin view
    public List<Map<String, Object>> getAllUsers() {
        String url = authServiceUrl + "/api/users/list_users";

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }

    public void promoteUser(String email, String role) {
        String url = authServiceUrl + "/api/users/promote";
        Map<String, String> payload = Map.of("email", email, "role", role);
        restTemplate.put(url, payload);
    }

    public List<UserDto> getUsersByDepartment(String department) {
        String url = authServiceUrl + "/api/users?department=" + department;
        ResponseEntity<List<UserDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDto>>() {}
        );
        return response.getBody();
    }


    public List<UserDto> getUsersByEmails(List<String> emails) {
        String url = authServiceUrl + "/api/users/batch";
        HttpEntity<List<String>> request = new HttpEntity<>(emails);
        ResponseEntity<List<UserDto>> response = restTemplate.exchange(
                url, HttpMethod.POST, request,
                new ParameterizedTypeReference<List<UserDto>>() {});
        return response.getBody();
    }

}
