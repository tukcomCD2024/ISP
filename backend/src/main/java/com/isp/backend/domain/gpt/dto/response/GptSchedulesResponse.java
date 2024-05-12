package com.isp.backend.domain.gpt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptSchedulesResponse {
    private String countryImage;
    private List<GptScheduleResponse> schedules;
}
