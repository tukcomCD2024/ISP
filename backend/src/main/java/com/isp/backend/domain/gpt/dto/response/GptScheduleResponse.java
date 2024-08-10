package com.isp.backend.domain.gpt.dto.response;

import com.isp.backend.domain.gpt.entity.GptSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GptScheduleResponse {
    private List<GptSchedule> schedule;
}
