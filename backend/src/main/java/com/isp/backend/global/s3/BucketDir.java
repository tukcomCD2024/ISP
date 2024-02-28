package com.isp.backend.global.s3;

import lombok.Getter;

@Getter
public enum BucketDir {
    IMAGE("HowAboutTrip-Backend-Image"),         // 상품 사진
    PHOTO("HowAboutTrip-Backend-Photo"), ;        // 여행 중 포토

    private String dirName;

    BucketDir(String dirName) {
        this.dirName = dirName;
    }

}
