package com.isp.backend.domain.schedule.dto.request;

import com.isp.backend.domain.country.entity.Country;
import com.isp.backend.domain.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequest {
    private Country country;
    private String purpose;
    private String startDate;
    private String endDate;

    public Schedule toEntity() {
        return Schedule.builder()
                .country(country)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
