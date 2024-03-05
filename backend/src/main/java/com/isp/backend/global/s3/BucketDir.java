package com.isp.backend.global.s3;

import lombok.Getter;

@Getter
public enum BucketDir {
    IMAGE("HowAboutTrip-Image"),         // 상품 사진
    PHOTO("HowAboutTrip-Photo"),
    ;        // 여행 중 포토

    private String dirName;

    BucketDir(String dirName) {
        this.dirName = dirName;
    }

}
