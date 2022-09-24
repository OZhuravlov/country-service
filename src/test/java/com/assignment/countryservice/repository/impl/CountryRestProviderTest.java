package com.assignment.countryservice.repository.impl;

import com.assignment.countryservice.FileUtil;
import com.assignment.countryservice.data.Country;
import com.assignment.countryservice.exception.CountryRetrievalException;
import com.assignment.countryservice.repository.CountryProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CountryRestProviderTest {

    @Test
    public void shouldReturnCountries() throws IOException {
        // setup
        final String responseBody = FileUtil.readDataFromFile("json/countries-orig.json");
        final List<Country> expectedCountries = FileUtil.getListFromJsonFile("json/countries-orig.json",
                Country.class);

        final WebClient webClient = WebClient.builder()
                .exchangeFunction(clientRequest -> buildClientResponse(responseBody))
                .build();
        final CountryProvider service = new CountryRestProvider(webClient);

        // execute
        final Flux<Country> countriesResponse = service.getCountries();

        // verify
        StepVerifier.create(countriesResponse)
                .expectNextMatches(country -> country.equals(expectedCountries.get(0)))
                .expectNextMatches(country -> country.equals(expectedCountries.get(1)))
                .verifyComplete();
    }

    @Test
    public void shouldThrowException_whenCantRetrieveCountries() throws IOException {
        // setup
        final String responseBody = FileUtil.readDataFromFile("json/countries-orig-bad.json");
        final WebClient webClient = WebClient.builder()
                .exchangeFunction(clientRequest -> buildClientResponse(responseBody))
                .build();
        final CountryProvider service = new CountryRestProvider(webClient);

        // execute and verify
        StepVerifier
                .create(service.getCountries())
                .expectError(CountryRetrievalException.class)
                .verify();
    }

    private static Mono<ClientResponse> buildClientResponse(String responseBody) {
        return Mono.just(ClientResponse.create(HttpStatus.OK)
                .header("content-type", "application/json")
                .body(responseBody)
                .build());
    }

}
