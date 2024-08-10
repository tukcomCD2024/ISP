package com.isp.backend.domain.country.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExchangeRateResponse {
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal rate;
}
