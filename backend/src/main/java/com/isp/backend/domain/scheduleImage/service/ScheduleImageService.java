package com.isp.backend.domain.scheduleImage.service;

import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.schedule.repository.ScheduleRepository;
import com.isp.backend.domain.scheduleImage.dto.request.SaveScheduleImageRequest;
import com.isp.backend.domain.scheduleImage.dto.response.ReadScheduleImageResponse;
import com.isp.backend.domain.scheduleImage.dto.response.SaveScheduleImageResponse;
import com.isp.backend.domain.scheduleImage.entity.ScheduleImage;
import com.isp.backend.domain.scheduleImage.repository.ScheduleImageRepository;
import com.isp.backend.domain.scheduleImage.repository.ScheduleImageS3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleImageService implements SaveScheduleImageService, ReadScheduleImageService, DeleteScheduleImageService {

	private final ScheduleImageS3Repository scheduleImageS3Repository;
	private final ScheduleRepository scheduleRepository;
	private final ScheduleImageRepository scheduleImageRepository;

	@Override
	public SaveScheduleImageResponse save(SaveScheduleImageRequest request, MultipartFile image) {
		String imagePath = scheduleImageS3Repository.save(request, image);
		Schedule schedule = scheduleRepository.findById(request.getScheduleId()).orElseThrow(IllegalArgumentException::new);
		ScheduleImage scheduleImage = new ScheduleImage(schedule, imagePath);

		scheduleImageRepository.save(scheduleImage);
		return new SaveScheduleImageResponse(scheduleImage);
	}

	@Override
	public ReadScheduleImageResponse read(Long scheduleId) {
		List<ScheduleImage> scheduleImages = scheduleImageRepository.findByScheduleId(scheduleId);
		Map<String, String> pathSaveDateMap = new HashMap<>();

		scheduleImages.forEach(scheduleImage ->
				pathSaveDateMap.put(scheduleImage.getSaveDate(), scheduleImage.getPath())
		);

		return new ReadScheduleImageResponse(scheduleId, pathSaveDateMap);
	}

	@Override
	public ResponseEntity<Void> delete(Long scheduleImageId) {
		if (!scheduleImageRepository.existsById(scheduleImageId)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		scheduleImageRepository.deleteById(scheduleImageId);
		return ResponseEntity.noContent().build();
	}
}