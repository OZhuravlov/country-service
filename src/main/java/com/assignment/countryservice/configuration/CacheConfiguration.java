package com.assignment.countryservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfiguration {

    @Value("${country.cache.duration-in-minutes}")
    private Integer countryCacheDurationInMinutes;

    @Bean("countryCacheDurationInMinutes")
    public Duration countryCacheDurationInMinutes() {
        return Duration.ofMinutes(countryCacheDurationInMinutes);
    }
}
