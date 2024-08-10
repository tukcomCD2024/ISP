package com.isp.backend.global.security;

import com.amadeus.Amadeus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmadeusConfig {

    @Value("${api-key.amadeus.accessKey}")
    private String apiKey;

    @Value("${api-key.amadeus.secretKey}")
    private String apiSecret;

    @Bean
    public Amadeus getAmadeus(){
        return Amadeus
                .builder(apiKey, apiSecret)
                .build();
    }

}