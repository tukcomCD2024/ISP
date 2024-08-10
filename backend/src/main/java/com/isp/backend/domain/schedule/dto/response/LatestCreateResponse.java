package com.isp.backend.domain.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LatestCreateResponse {

    private Long id;

    private String scheduleName;

    private String city; // 여행지 장소

    private String imageUrl; // 이미지 url

    private List<String> plan ; // 여행 일정 리스트

}
