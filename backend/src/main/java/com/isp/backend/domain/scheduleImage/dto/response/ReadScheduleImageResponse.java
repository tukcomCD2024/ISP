package com.isp.backend.domain.scheduleImage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ReadScheduleImageResponse {
    private Long scheduleId;
    private Map<String, String> pathAndSaveDate;
}