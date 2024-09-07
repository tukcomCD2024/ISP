package com.isp.backend.domain.scheduleImage.service;

import com.isp.backend.domain.gpt.entity.Coordinate;
import com.isp.backend.domain.schedule.dto.response.ScheduleListResponse;
import com.isp.backend.domain.schedule.entity.Schedule;
import com.isp.backend.domain.schedule.repository.ScheduleRepository;
import com.isp.backend.domain.scheduleImage.dto.request.SaveScheduleImageRequest;
import com.isp.backend.domain.scheduleImage.dto.response.*;
import com.isp.backend.domain.scheduleImage.entity.ScheduleImage;
import com.isp.backend.domain.scheduleImage.repository.ScheduleImageRepository;
import com.isp.backend.domain.scheduleImage.repository.ScheduleImageS3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleImageService implements SaveScheduleImageService, ReadScheduleImageService, DeleteScheduleImageService, ReadScheduleImageCountriesService {

	private final ScheduleImageS3Repository scheduleImageS3Repository;
	private final ScheduleRepository scheduleRepository;
	private final ScheduleImageRepository scheduleImageRepository;

	@Override
	public SaveScheduleImageResponse save(SaveScheduleImageRequest request, MultipartFile image) {
		String imagePath = scheduleImageS3Repository.save(request, image);
		Schedule schedule = scheduleRepository.findById(request.getScheduleId()).orElseThrow(IllegalArgumentException::new);
		ScheduleImage scheduleImage = new ScheduleImage(schedule, request.getSaveDate(), imagePath);

		scheduleImageRepository.save(scheduleImage);
		return new SaveScheduleImageResponse(scheduleImage);
	}


	@Override
	public ReadScheduleImageCountriesResponse readCountries(List<ScheduleListResponse> schedules) {
		Map<Coordinate, List<Long>> locationSchedules = new HashMap<>();

		for (ScheduleListResponse schedule : schedules) {
			Coordinate coordinate = new Coordinate(schedule.getLatitude(), schedule.getLongitude());
			locationSchedules.computeIfAbsent(coordinate, k -> new ArrayList<>()).add(schedule.getId());
		}

		List<ScheduleLocationResponse> responses = locationSchedules.entrySet().stream()
				.map(entry -> new ScheduleLocationResponse(
						entry.getKey().getLatitude(),
						entry.getKey().getLongitude(),
						entry.getValue()))
				.collect(Collectors.toList());

		return new ReadScheduleImageCountriesResponse(responses);
	}

	@Override
	public ReadScheduleImageResponse read(Long scheduleId) {
		List<ScheduleImage> scheduleImages = scheduleImageRepository.findByScheduleId(scheduleId);
		List<ScheduleImageResponse> scheduleImageResponses = scheduleImages.stream()
				.map(scheduleImage -> new ScheduleImageResponse(scheduleImage.getId(), scheduleImage.getPath(), scheduleImage.getSaveDate()))
				.collect(Collectors.toList());

		return new ReadScheduleImageResponse(scheduleId, scheduleImageResponses);
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