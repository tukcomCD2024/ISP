package com.isp.backend.global.s3.controller;

import com.isp.backend.global.s3.service.S3ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/images")
public class S3ImageController {
    S3ImageService s3ImageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestParam("file") MultipartFile file,
                         @RequestParam("directory") String directory) {
        return s3ImageService.create(file, directory);
    }

}