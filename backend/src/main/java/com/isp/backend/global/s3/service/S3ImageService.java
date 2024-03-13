package com.isp.backend.global.s3.service;

import com.isp.backend.global.s3.repository.S3ImageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class S3ImageService {
    S3ImageRepository s3ImageRepository;

    public String create(MultipartFile file) {
        return s3ImageRepository.save(file);
    }

    public String get(String fileName) {
        return s3ImageRepository.find(fileName);
    }
}