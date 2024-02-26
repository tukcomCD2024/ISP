package com.isp.backend.domain.schedule.dto;

import com.isp.backend.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDTO {
    private String name;
    private String email;

    public ScheduleResponseDTO(Schedule schedule) {
    }
}
