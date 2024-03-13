package com.isp.backend.domain.gpt.dto;

import com.isp.backend.domain.gpt.entity.GptSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptScheduleResponseDto {
    private String countryImage;
    private List<GptSchedule> schedules;
}
