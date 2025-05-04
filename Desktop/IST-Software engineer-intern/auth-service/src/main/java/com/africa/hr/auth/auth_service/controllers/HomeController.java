package com.africa.hr.auth.auth_service.controllers;
import com.africa.hr.auth.auth_service.Dto.UserDto;
import com.africa.hr.auth.auth_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class HomeController {

    @Autowired
    private UserService userService;


}

