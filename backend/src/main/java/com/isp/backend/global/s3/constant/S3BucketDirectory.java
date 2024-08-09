package com.isp.backend.global.s3.constant;

import com.isp.backend.global.exception.Image.DirectoryNameNotFoundException;
import lombok.Getter;

@Getter
public enum S3BucketDirectory {
    IMAGE("HowAboutTrip-Backend-Image/"),         // 나라 사진
    PHOTO("HowAboutTrip-Backend-Photo/"),         // 여행 중 포토
    RECEIPT("HowAboutTrip-Backend-Receipt/"),     // 여행 영수증
    WEATHER("HowAboutTrip-Backend-Weather/"), ;   // 날씨 아이콘

    private final String directory;

    S3BucketDirectory(String directory) {
        this.directory = directory;
    }


    // 입력된 s3 디렉토리명이 유효한지 확인
    public static boolean isValidDirectory(String directory) {
        for (S3BucketDirectory bucketDirectory : values()) {
            if (bucketDirectory.name().equalsIgnoreCase(directory)) {
                return true;
            }
        }
        return false;
    }

    //  디렉터리 이름에 대응하는 실제 디렉터리 경로를 반환
    public static String getDirectoryByName(String name) {
        for (S3BucketDirectory bucketDirectory : values()) {
            if (bucketDirectory.name().equalsIgnoreCase(name)) {
                return bucketDirectory.getDirectory();
            }
        }
        throw new DirectoryNameNotFoundException();
    }

}
