package com.isp.backend.domain.country.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRateService {


    @Value("${api-key.exchange-rate}")
    private String exchangeRateApiKey;

    private final String BASE_URL = "https://v6.exchangerate-api.com/v6";

    /** 특정 기준 통화에 대한 모든 환율 정보 가져오기 **/
//    public Map<String, Double> getExchangeRates(String baseCurrency) {
//        String url = BASE_URL + exchangeRateApiKey + "/latest/" + baseCurrency;
//        logger.info("Requesting exchange rates for base currency: {}", baseCurrency);
//
//        try {
//            String jsonResponse = restTemplate.getForObject(url, String.class);
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(jsonResponse);
//            logger.info("Received response: {}", jsonResponse);
//
//            String result = rootNode.path("result").asText();
//            if (!"success".equals(result)) {
//                throw new RuntimeException("API 요청 실패: " + result);
//            }
//
//            JsonNode conversionRates = rootNode.path("conversion_rates");
//            Map<String, Double> rates = new HashMap<>();
//            conversionRates.fields().forEachRemaining(entry -> {
//                rates.put(entry.getKey(), entry.getValue().asDouble());
//            });
//
//            return rates;
//        } catch (IOException e) {
//            logger.error("Error parsing JSON response", e);
//            throw new RuntimeException("환율 정보를 가져오는데 실패했습니다.", e);
//        }
//    }



}
