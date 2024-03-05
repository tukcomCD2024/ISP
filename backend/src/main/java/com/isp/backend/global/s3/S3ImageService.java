package com.isp.backend.global.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class S3ImageService {

    private final S3Service s3Service;

    @Transactional
    public String reUploadImage(BucketDir bucketDir, MultipartFile newImage, String oriImage) throws IOException {
        if (newImage == null || newImage.isEmpty()) {
            return oriImage;
        }
        s3Service.deleteImage(oriImage);
        return s3Service.uploadImage(bucketDir, newImage);
    }
}
