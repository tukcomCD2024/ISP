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


}
