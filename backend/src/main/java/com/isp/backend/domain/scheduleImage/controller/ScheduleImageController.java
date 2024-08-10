package com.isp.backend.domain.scheduleImage.controller;

import com.isp.backend.domain.scheduleImage.dto.SaveScheduleImageRequest;
import com.isp.backend.domain.scheduleImage.dto.SaveScheduleImageResponse;
import com.isp.backend.domain.scheduleImage.service.SaveImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scheduleImages")
public class ScheduleImageController {

    private final SaveImageService saveImageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaveScheduleImageResponse create(@RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "saveScheduleImageRequest") SaveScheduleImageRequest request) {
        return saveScheduleImageService.save(request, image);
    }

    @GetMapping
    }
}