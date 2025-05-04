package com.africa.hr.leave.leave_service.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "drhjvmsop",
                "api_key", "865876477547284",
                "api_secret", "1d9awrTuVdb3OdmYt8zl62GLzds"
        ));
    }
}

