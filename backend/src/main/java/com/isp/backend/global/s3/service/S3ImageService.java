package com.isp.backend.global.s3.service;

import com.isp.backend.global.exception.Image.DirectoryNameNotFoundException;
import com.isp.backend.global.s3.constant.S3BucketDirectory;
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

    public String create(MultipartFile file, String directory) {
        String mappedDirectory = mapDirectory(directory);
        return s3ImageRepository.save(file, mappedDirectory);
    }

    public String get(String directory, String fileName) {
        return s3ImageRepository.find(directory, fileName);
    }

    private String mapDirectory(String directory) {
        if (S3BucketDirectory.valueOf(directory) == S3BucketDirectory.IMAGE ||
                S3BucketDirectory.valueOf(directory) == S3BucketDirectory.PHOTO) {
            return S3BucketDirectory.valueOf(directory).getDirectory();
        } else {
            throw new DirectoryNameNotFoundException();
        }
    }

}