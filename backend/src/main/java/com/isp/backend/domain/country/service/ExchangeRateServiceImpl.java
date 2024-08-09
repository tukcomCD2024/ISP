package com.isp.backend.domain.country.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.isp.backend.domain.country.dto.response.ExchangeRateResponse;
import com.isp.backend.domain.country.entity.ExchangeRate;
import com.isp.backend.domain.country.mapper.ExchangeRateMapper;
import com.isp.backend.domain.country.repository.ExchangeRateRepository;
import com.isp.backend.global.exception.openApi.ExchangeRateIsFailedException;
import com.isp.backend.global.exception.openApi.ExchangeRateSearchFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateMapper exchangeRateMapper ;

    @Value("${api-key.exchange-rate}")
    private String exchangeRateApiKey;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository, ExchangeRateMapper exchangeRateMapper) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateMapper = exchangeRateMapper;
    }

    /** 환율 데이터 가져와서 업데이트 하는 API 메서드 **/
    @Override
    @Transactional
    public void updateExchangeRates() {
        // 한국과 미국 통화 환율 비율 저장
        updateRatesForBaseCurrency("KRW");
        updateRatesForBaseCurrency("USD");
    }


    /** 환율 데이터 가져와서 업데이트 하는 API 메서드 **/
    @Override
    public List<ExchangeRateResponse> getAllExchangeRates() {
        try {
            // DB에서 모든 환율 데이터를 가져옴
            List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll();

            // baseCurrency가 "KRW" 또는 "USD"인 데이터만 필터링하고 DTO로 변환
            return exchangeRates.stream()
                    .filter(rate -> ("KRW".equals(rate.getBaseCurrency()) || "USD".equals(rate.getBaseCurrency()))
                            && !rate.getBaseCurrency().equals(rate.getTargetCurrency()))
                    .map(exchangeRateMapper::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ExchangeRateIsFailedException() ;
        }
    }


    /** 측정 baseCurrency 통화에 대해 업데이트 하는 메서드**/
    private void updateRatesForBaseCurrency(String baseCurrency) {
        String exchangeRateAPI_URL = "https://v6.exchangerate-api.com/v6/" + exchangeRateApiKey + "/latest/" + baseCurrency;

        try {
            URL url = new URL(exchangeRateAPI_URL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // API 응답 데이터를 JsonObject로 변환
            Gson gson = new Gson();
            JsonObject jsonobj = gson.fromJson(new InputStreamReader(request.getInputStream()), JsonObject.class);

            // API 응답 결과
            String req_result = jsonobj.get("result").getAsString();

            if ("success".equals(req_result)) {
                JsonObject conversionRates = jsonobj.getAsJsonObject("conversion_rates");
                for (String targetCurrency : conversionRates.keySet()) {
                    // 필요한 통화만 가져온다
                    if (isSupportedCurrency(targetCurrency)) {
                        double rate = conversionRates.get(targetCurrency).getAsDouble();

                        // DB에서 환율 데이터 존재 여부 확인
                        ExchangeRate existingRate = exchangeRateRepository.findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency);

                        if (existingRate == null) {
                            ExchangeRate newExchangeRate = new ExchangeRate(baseCurrency, targetCurrency, rate);
                            exchangeRateRepository.save(newExchangeRate);
                        } else {
                            // 기록이 이미 존재하면, rate만 수정한다.
                            existingRate.setRate(rate);
                            exchangeRateRepository.save(existingRate);
                        }
                    }
                }
            } else {
                throw new ExchangeRateSearchFailedException();
            }
        } catch (Exception e) {
            throw new ExchangeRateIsFailedException() ;
        }
    }

    /** 지원하는 통화만 가져오는 메서드 **/
    private boolean isSupportedCurrency(String currencyCode) {
        return Set.of("JPY", "GBP", "EUR", "CHF", "CZK", "USD", "SGD", "TWD", "LAK", "MYR", "VND", "THB", "IDR", "PHP", "KRW").contains(currencyCode);
    }




}
