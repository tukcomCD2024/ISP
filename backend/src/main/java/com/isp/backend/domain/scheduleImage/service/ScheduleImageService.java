package com.isp.backend.domain.scheduleImage.service;

import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.schedule.repository.ScheduleRepository;
import com.isp.backend.domain.scheduleImage.dto.SaveScheduleImageRequest;
import com.isp.backend.domain.scheduleImage.dto.SaveScheduleImageResponse;
import com.isp.backend.domain.scheduleImage.entity.ScheduleImage;
import com.isp.backend.domain.scheduleImage.repository.ScheduleImageRepository;
import com.isp.backend.domain.scheduleImage.repository.ScheduleImageS3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ScheduleImageService implements SaveImageService {

	private final ScheduleImageS3Repository scheduleImageS3Repository;
	private final ScheduleRepository scheduleRepository;
	private final ScheduleImageRepository scheduleImageRepository;

	@Override
	public SaveScheduleImageResponse save(SaveScheduleImageRequest request, MultipartFile image) {
		String imagePath = scheduleImageS3Repository.save(request, image);
		Schedule schedule = scheduleRepository.findById(request.scheduleId()).orElseThrow(IllegalArgumentException::new);
		ScheduleImage scheduleImage = new ScheduleImage(schedule, imagePath);

		scheduleImageRepository.save(scheduleImage);
		return new SaveScheduleImageResponse(scheduleImage);
	}
}