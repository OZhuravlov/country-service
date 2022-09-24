package com.assignment.countryservice.service;

import com.assignment.countryservice.data.Country;
import com.assignment.countryservice.data.dto.CountryDto;

import java.util.Optional;

public class CountryTransformer {

    public static CountryDto toCountryDetailDto(final Country country) {
        return CountryDto.builder()
                .name(country.getName())
                .countryCode(country.getAlpha3Code())
                .capital(country.getCapital())
                .population(country.getPopulation())
                .flagFileUrl(Optional.ofNullable(country.getFlags())
                        .map(Country.CountryFlags::getSvg)
                        .orElse(null))
                .build();
    }
}
