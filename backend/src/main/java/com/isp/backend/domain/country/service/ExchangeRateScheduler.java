package com.isp.backend.domain.country.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateScheduler {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Seoul") // 매일 한국시간 새벽 2시에 실행
    public void scheduleExchangeRateUpdate() {
        try {
            exchangeRateService.updateExchangeRates();
        } catch (Exception e) {
            // 예외 처리 로직
            System.out.println("Failed to update exchange rates: " + e.getMessage());
        }
    }
}