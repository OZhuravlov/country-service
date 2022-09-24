package com.assignment.countryservice.data.dto;

import com.assignment.countryservice.web.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CountryDto {
    @JsonView(Views.Public.class)
    private String name;

    @JsonView(Views.Public.class)
    @JsonProperty("country_code")
    private String countryCode;

    @JsonView(Views.CountryDetails.class)
    private String capital;

    @JsonView(Views.CountryDetails.class)
    private Number population;

    @JsonView(Views.CountryDetails.class)
    @JsonProperty("flag_file_url")
    private URL flagFileUrl;
}
