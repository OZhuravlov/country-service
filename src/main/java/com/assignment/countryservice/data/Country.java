package com.assignment.countryservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    private String name;
    private String alpha3Code;
    private String capital;
    private Number population;
    private CountryFlags flags;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CountryFlags {
        private URL svg;
        private URL png;
    }
}
