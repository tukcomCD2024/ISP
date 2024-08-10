package com.isp.backend.domain.scheduleImage.service;

import com.isp.backend.domain.scheduleImage.dto.SaveScheduleImageRequest;
import com.isp.backend.domain.scheduleImage.dto.SaveScheduleImageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface SaveImageService {

	SaveScheduleImageResponse save(SaveScheduleImageRequest request, MultipartFile image);
}