package com.isp.backend.global.s3.repository;

import com.isp.backend.global.s3.constant.S3BucketDirectory;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class S3ImageRepository {
    S3Template s3template;
    String bucketName;

    public S3ImageRepository(S3Template s3template, @Value("${spring.cloud.aws.s3.bucket}") String bucketName) {
        this.s3template = s3template;
        this.bucketName = bucketName;
    }

    public String save(MultipartFile file, String directory) {
        String uniqueFileName =  UUID.randomUUID() + "-" + file.getOriginalFilename() ;
        String key = directory + uniqueFileName;
        final S3Resource result = s3template.upload(bucketName, key, getInputStream(file));
        return getUrl(result);
    }

    public String find(String directory, String filename) {
        String key = directory + filename;
        final S3Resource result = s3template.download(bucketName, key);
        return getUrl(result);
    }

    private InputStream getInputStream(@RequestParam("file") MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private String getUrl(S3Resource s3Resource) {
        try {
            return s3Resource.getURL().toString();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
