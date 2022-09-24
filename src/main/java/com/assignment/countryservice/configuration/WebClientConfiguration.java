package com.assignment.countryservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Value("${country.base-url}")
    private String countryBaseUrl;

    @Bean("webClient")
    public WebClient webClient() {
        return WebClient.create(countryBaseUrl);
    }
}
