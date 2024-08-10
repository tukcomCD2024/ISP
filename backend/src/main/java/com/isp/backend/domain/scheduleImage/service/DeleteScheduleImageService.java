package com.isp.backend.domain.scheduleImage.service;

import org.springframework.http.ResponseEntity;

public interface DeleteScheduleImageService {
    ResponseEntity<Void> delete(Long scheduleImageId);
}