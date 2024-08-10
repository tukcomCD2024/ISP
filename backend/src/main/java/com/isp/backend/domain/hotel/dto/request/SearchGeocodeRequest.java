package com.isp.backend.domain.hotel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SearchGeocodeRequest {

    private String latitude;

    private String longitude;

    private int radius ;

    private String ratings ; // 호텔 등급

}
