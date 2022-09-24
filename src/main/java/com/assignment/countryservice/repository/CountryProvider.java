package com.assignment.countryservice.repository;

import com.assignment.countryservice.data.Country;
import reactor.core.publisher.Flux;

public interface CountryProvider {
    Flux<Country> getCountries();
}
