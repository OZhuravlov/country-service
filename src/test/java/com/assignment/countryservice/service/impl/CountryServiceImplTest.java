package com.assignment.countryservice.service.impl;

import com.assignment.countryservice.FileUtil;
import com.assignment.countryservice.data.Country;
import com.assignment.countryservice.data.dto.CountryDto;
import com.assignment.countryservice.exception.CountryNotFoundException;
import com.assignment.countryservice.repository.CountryProvider;
import com.assignment.countryservice.service.CountryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTest {

    private static final String NOT_FOUND_ERROR_MESSAGE = "Country with name <Albania> not found";

    private CountryProvider provider;

    private CountryService service;

    @Before
    public void setUp() {
        provider = mock(CountryProvider.class);
        service = new CountryServiceImpl(provider, Duration.ofMinutes(10));
    }

    @Test
    public void shouldReturnCountries() throws IOException {
        // setup
        final List<CountryDto> expectedCountries = FileUtil.getListFromJsonFile("json/country-detail-dtos.json",
                CountryDto.class);

        when(provider.getCountries()).thenReturn(buildCountryProviderResponse());

        // execute
        final Flux<CountryDto> resultCountries = service.getCountries();

        // verify
        StepVerifier.create(resultCountries)
                .expectNextMatches(country -> country.equals(expectedCountries.get(0)))
                .expectNextMatches(country -> country.equals(expectedCountries.get(1)))
                .verifyComplete();
    }

    @Test
    public void shouldFindCountryByExactName() throws IOException {
        // setup
        final CountryDto expectedCountry = FileUtil.getObjectFromJsonFile("json/country-detail-dto.json",
                CountryDto.class);
        final String name = "Afghanistan";

        when(provider.getCountries()).thenReturn(buildCountryProviderResponse());

        // execute
        final Mono<CountryDto> resultCountry = service.getCountry(name);

        // verify
        StepVerifier.create(resultCountry)
                .expectNextMatches(country -> country.equals(expectedCountry))
                .verifyComplete();
    }

    @Test
    public void shouldFindCountryByNameCaseInsensitive() throws IOException {
        // setup
        final CountryDto expectedCountry = FileUtil.getObjectFromJsonFile("json/country-detail-dto.json",
                CountryDto.class);
        final String name = "AfGhAnIstan";

        when(provider.getCountries()).thenReturn(buildCountryProviderResponse());

        // execute
        final Mono<CountryDto> resultCountry = service.getCountry(name);

        // verify
        StepVerifier.create(resultCountry)
                .expectNextMatches(country -> country.equals(expectedCountry))
                .verifyComplete();
    }


    @Test
    public void shouldFail_whenCountryNotFound() throws IOException {
        // setup
        final String name = "Albania";

        when(provider.getCountries()).thenReturn(buildCountryProviderResponse());

        // execute and verify
        StepVerifier
                .create(service.getCountry(name))
                .expectErrorMatches(throwable -> throwable instanceof CountryNotFoundException &&
                        NOT_FOUND_ERROR_MESSAGE.equals(throwable.getMessage()))
                .verify();
    }

    private Flux<Country> buildCountryProviderResponse() throws IOException {
        final List<Country> countryList = FileUtil.getListFromJsonFile("json/countries-orig.json", Country.class);
        return Mono.just(countryList).flatMapMany(Flux::fromIterable);
    }
}
