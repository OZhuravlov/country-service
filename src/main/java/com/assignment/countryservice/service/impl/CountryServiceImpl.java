package com.assignment.countryservice.service.impl;

import com.assignment.countryservice.data.dto.CountryDto;
import com.assignment.countryservice.exception.CountryNotFoundException;
import com.assignment.countryservice.repository.CountryProvider;
import com.assignment.countryservice.service.CountryService;
import com.assignment.countryservice.service.CountryTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryServiceImpl implements CountryService {

    private static final String NOT_FOUND_ERROR_MESSAGE = "Country with name <%s> not found";

    private final CountryProvider provider;
    private final Duration countryCacheDurationInMinutes;

    @Override
    public Flux<CountryDto> getCountries() {
        log.debug("Get Countries");
        return provider.getCountries()
                .map(CountryTransformer::toCountryDetailDto)
                .cache(countryCacheDurationInMinutes);
    }

    @Override
    public Mono<CountryDto> getCountry(final String name) {
        final String nameNormalized = name.trim().toUpperCase();
        log.debug("Get Country by normalized name {}", nameNormalized);
        return getCountryMap()
                .filter(e -> e.containsKey(nameNormalized))
                .map(e -> e.get(nameNormalized))
                .switchIfEmpty(Mono.error(new CountryNotFoundException(
                        String.format(NOT_FOUND_ERROR_MESSAGE, name)
                )));
    }

    private Mono<Map<String, CountryDto>> getCountryMap() {
        return getCountries().collectMap(e -> e.getName().toUpperCase(), Function.identity())
                .cache(countryCacheDurationInMinutes);
    }
}
