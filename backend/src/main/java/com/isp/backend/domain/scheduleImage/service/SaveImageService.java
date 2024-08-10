package com.isp.backend.domain.scheduleImage.service;

import com.isp.backend.domain.scheduleImage.dto.request.SaveScheduleImageRequest;
import com.isp.backend.domain.scheduleImage.dto.response.SaveScheduleImageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface SaveImageService {

	SaveScheduleImageResponse save(SaveScheduleImageRequest request, MultipartFile image);
}