package com.africa.hr.leave.leave_service.controllers;

import org.springframework.web.client.RestTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public-holidays")
public class PublicHolidayController {

    private final RestTemplate restTemplate;

    public PublicHolidayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/rwanda")
    public ResponseEntity<String> getRwandaHolidays() {
        String url = "https://www.timeanddate.com/holidays/rwanda/";
        String html = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(html);
    }
}

