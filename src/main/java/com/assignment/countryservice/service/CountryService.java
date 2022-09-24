package com.assignment.countryservice.service;

import com.assignment.countryservice.data.dto.CountryDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CountryService {

    Flux<CountryDto> getCountries();

    Mono<CountryDto> getCountry(String name);
}
