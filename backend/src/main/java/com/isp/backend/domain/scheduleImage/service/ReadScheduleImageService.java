package com.isp.backend.domain.scheduleImage.service;

import com.isp.backend.domain.scheduleImage.dto.response.ReadScheduleImageResponse;

public interface ReadScheduleImageService {
    ReadScheduleImageResponse read(Long scheduleId);
}