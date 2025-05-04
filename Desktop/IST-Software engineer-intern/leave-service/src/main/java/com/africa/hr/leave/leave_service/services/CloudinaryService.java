package com.africa.hr.leave.leave_service.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
    public CloudinaryService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "drhjvmsop",
                "api_key", "865876477547284",
                "api_secret", "1d9awrTuVdb3OdmYt8zl62GLzds"
        ));
    }

    //By default, Cloudinary assumes the file is an image, and tries to parse it as one.
    //Uploading .docx, .pdf, .txt, .zip, etc., without telling Cloudinary it's a raw file
    // causes it to throw Unsupported ZIP file or similar parsing errors.
    public String uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", "raw")
        );
        return uploadResult.get("secure_url").toString();
    }

}

