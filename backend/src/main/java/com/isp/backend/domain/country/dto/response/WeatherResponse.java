package com.isp.backend.domain.country.dto.response;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    private String main;
    private String description;

    @JsonProperty("temp_min")
    private String tempMin;

    @JsonProperty("temp_max")
    private String tempMax;

    private String temp;

    private String localTime;

    private String iconUrl ;

}
