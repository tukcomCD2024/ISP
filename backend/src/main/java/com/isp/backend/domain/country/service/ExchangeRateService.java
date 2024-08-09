package com.isp.backend.domain.country.service;

import com.isp.backend.domain.country.dto.response.ExchangeRateResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExchangeRateService {
    @Transactional
    void updateExchangeRates();
    List<ExchangeRateResponse> getAllExchangeRates();

}
