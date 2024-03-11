package com.isp.backend.global.s3.constant;

import lombok.Getter;

@Getter
public enum S3BucketDirectory {
    IMAGE("HowAboutTrip-Backend-Image/"),         // 상품 사진
    PHOTO("HowAboutTrip-Backend-Photo/"), ;        // 여행 중 포토

    private final String directory;

    S3BucketDirectory(String directory) {
        this.directory = directory;
    }

}
