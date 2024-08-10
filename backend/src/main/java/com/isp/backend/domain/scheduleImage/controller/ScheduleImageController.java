package com.isp.backend.domain.scheduleImage.controller;

import com.isp.backend.domain.scheduleImage.dto.request.SaveScheduleImageRequest;
import com.isp.backend.domain.scheduleImage.dto.response.ReadScheduleImageResponse;
import com.isp.backend.domain.scheduleImage.dto.response.SaveScheduleImageResponse;
import com.isp.backend.domain.scheduleImage.service.DeleteScheduleImageService;
import com.isp.backend.domain.scheduleImage.service.ReadScheduleImageService;
import com.isp.backend.domain.scheduleImage.service.SaveScheduleImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scheduleImages")
public class ScheduleImageController {

    private final SaveScheduleImageService saveScheduleImageService;
    private final ReadScheduleImageService readScheduleImageService;
    private final DeleteScheduleImageService deleteScheduleImageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaveScheduleImageResponse create(@RequestPart(value = "image", required = false) MultipartFile image,
                                            @RequestPart(value = "saveScheduleImageRequest") SaveScheduleImageRequest request) {
        return saveScheduleImageService.save(request, image);
    }

    @GetMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ReadScheduleImageResponse read(@PathVariable(name = "scheduleId") Long scheduleId) {
        return readScheduleImageService.read(scheduleId);
    }

    @DeleteMapping("/{scheduleImageId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> delete(@PathVariable(name = "scheduleImageId") Long scheduleImageId) {
        return deleteScheduleImageService.delete(scheduleImageId);
    }

}