package com.isp.backend.domain.country.mapper;

import com.isp.backend.domain.country.dto.response.ExchangeRateResponse;
import com.isp.backend.domain.country.entity.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ExchangeRateMapper {

    public ExchangeRateResponse convertToDto(ExchangeRate exchangeRate) {
        ExchangeRateResponse dto = new ExchangeRateResponse();
        dto.setBaseCurrency(exchangeRate.getBaseCurrency());
        dto.setTargetCurrency(exchangeRate.getTargetCurrency());
        dto.setRate(BigDecimal.valueOf(exchangeRate.getRate()));
        return dto;
    }

}
