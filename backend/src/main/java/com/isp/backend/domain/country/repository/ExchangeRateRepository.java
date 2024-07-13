package com.isp.backend.domain.country.repository;

import com.isp.backend.domain.country.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    ExchangeRate findByBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency);

}
