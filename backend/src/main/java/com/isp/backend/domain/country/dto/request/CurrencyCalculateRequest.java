package com.isp.backend.domain.country.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CurrencyCalculateRequest {

    private String baseCity;

    private double basePrice ;

    private String targetCity ;

}
