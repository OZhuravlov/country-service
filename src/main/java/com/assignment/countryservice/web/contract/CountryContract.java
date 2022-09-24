package com.assignment.countryservice.web.contract;

import com.assignment.countryservice.data.dto.CountryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface CountryContract {
    @Operation(summary = "Get all countries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Country list produced successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CountryDto.class))}),
            @ApiResponse(responseCode = "400", description = "Something wrong during country list retrieval",
                    content = @Content)})
    Mono<Map<String, List<CountryDto>>> getCountries();

    @Operation(summary = "Get a country details by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the country",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CountryDto.class))}),
            @ApiResponse(responseCode = "400", description = "Something wrong during country retrieval",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Country not found", content = @Content)})
    Mono<CountryDto> getCountry(String name);
}
