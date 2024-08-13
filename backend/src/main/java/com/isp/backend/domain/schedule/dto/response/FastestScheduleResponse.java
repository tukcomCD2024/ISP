package com.isp.backend.domain.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FastestScheduleResponse {

    private Long id;

    private String scheduleName; // 여행지 이름

    private String dday;

    private String country ;

    private String currencyName; //통화명

    private String imageUrl;

}
