package com.assignment.countryservice.web.controller;

import com.assignment.countryservice.data.dto.CountryDto;
import com.assignment.countryservice.service.CountryService;
import com.assignment.countryservice.web.Views;
import com.assignment.countryservice.web.contract.CountryContract;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class CountryController implements CountryContract {
    private static final String COUNTRIES_MAP_KEY = "countries";

    private final CountryService countryService;

    @Override
    @JsonView(Views.Public.class)
    @GetMapping(value = "/countries", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, List<CountryDto>>> getCountries() {
        log.info("Get Country list");
        return countryService.getCountries()
                .collectList()
                .map(list -> Collections.singletonMap(COUNTRIES_MAP_KEY, list));
    }

    @Override
    @JsonView(Views.CountryDetails.class)
    @GetMapping(value = "/countries/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CountryDto> getCountry(@PathVariable final String name) {
        log.info("Get Country by name {}", name);
        return countryService.getCountry(name);
    }
}
