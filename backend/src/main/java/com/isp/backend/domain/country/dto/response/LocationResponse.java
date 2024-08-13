package com.isp.backend.domain.country.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LocationResponse {

    private double latitude;

    private double longitude;
    private String currencyName ;

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

}
