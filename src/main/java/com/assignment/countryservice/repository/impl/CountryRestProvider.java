package com.assignment.countryservice.repository.impl;

import com.assignment.countryservice.data.Country;
import com.assignment.countryservice.exception.CountryRetrievalException;
import com.assignment.countryservice.repository.CountryProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class CountryRestProvider implements CountryProvider {

    private static final String GET_COUNTRIES_URL = "/all?fields=name,alpha3Code,capital,population,flags";

    private final WebClient webClient;

    @Override
    public Flux<Country> getCountries() {
        log.debug("get countries url {}", GET_COUNTRIES_URL);
        return webClient.get()
                .uri(GET_COUNTRIES_URL)
                .retrieve()
                .bodyToFlux(Country.class)
                .onErrorMap(Exception.class, (error)
                        -> new CountryRetrievalException(error.getMessage(), error.getCause()));
    }
}
