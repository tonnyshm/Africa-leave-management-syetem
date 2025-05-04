package com.africa.hr.leave.leave_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {  // defining RestTemplate bean in your LeaveServiceApplication
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

