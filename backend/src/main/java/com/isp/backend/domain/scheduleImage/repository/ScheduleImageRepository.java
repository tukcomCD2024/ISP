package com.isp.backend.domain.scheduleImage.repository;

import com.isp.backend.domain.scheduleImage.entity.ScheduleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleImageRepository extends JpaRepository<ScheduleImage, Long> {
    List<ScheduleImage> findByScheduleId(Long scheduleId);
}