package com.isp.backend.domain.scheduleImage.repository;

import com.isp.backend.domain.scheduleImage.dto.SaveScheduleImageRequest;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScheduleImageS3Repository {
	private static final String IMAGE = "HowAboutTrip-Backend-ScheduleImage/";
	private static final String SLASH = "/";

	private final S3Template s3template;
	private final String bucketName;

	public ScheduleImageS3Repository(S3Template s3template,
									 @Value("${spring.cloud.aws.s3.bucket}") String bucketName) {
		this.s3template = s3template;
		this.bucketName = bucketName;
	}

	public String save(SaveScheduleImageRequest request, MultipartFile image) {
		String path = IMAGE + request.getScheduleId() + SLASH + image.getOriginalFilename();
        S3Resource result = s3template.upload(bucketName, path, getInputStream(image));
        return getUrl(result);
    }

	private InputStream getInputStream(MultipartFile image) {
		try {
			return image.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException("Error getting input stream from MultipartFile", e);
		}
	}

	private String getUrl(S3Resource s3Resource) {
		try {
			return s3Resource.getURL().toString();
		} catch (IOException e) {
			throw new RuntimeException("Error getting URL from S3Resource", e);
		}
	}
}