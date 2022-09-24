package com.assignment.countryservice.service;

import com.assignment.countryservice.FileUtil;
import com.assignment.countryservice.data.Country;
import com.assignment.countryservice.data.dto.CountryDto;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CountryTransformerTest {

    @Test
    void shouldTransformCountryToCountryDetailDto() throws IOException {
        // setup
        final Country countryOriginal = FileUtil.getObjectFromJsonFile("json/country-orig.json", Country.class);
        final CountryDto expectedCountryDto = FileUtil.getObjectFromJsonFile("json/country-detail-dto.json", CountryDto.class);

        // execute
        final CountryDto countryDto = CountryTransformer.toCountryDetailDto(countryOriginal);

        // verify
        assertEquals(expectedCountryDto, countryDto);
    }
}
