package com.assignment.countryservice.integration;

import com.assignment.countryservice.FileUtil;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock
@ActiveProfiles("integration-tests")
public class CountryControllerIT {

    private static final String GET_ALL_COUNTRIES_DATA_URL = "/restcountries/v2/all" +
            "?fields=name,alpha3Code,capital,population,flags";
    private static final String GET_COUNTRIES_URL = "/countries";
    private static final String VALID_COUNTRY_NAME = "Afghanistan";
    private static final String INVALID_COUNTRY_NAME = "Alban";
    private static final String GET_VALID_COUNTRY_BY_NAME_URL = GET_COUNTRIES_URL + "/" + VALID_COUNTRY_NAME;
    private static final String GET_INVALID_COUNTRY_BY_NAME_URL = GET_COUNTRIES_URL + "/" + INVALID_COUNTRY_NAME;

    public static final String APPLICATION_JSON_CHARSET = "application/json";

    @LocalServerPort
    private int port;

    @Test
    void shouldReturnAllCountries() throws IOException {
        final String countriesDataJson = FileUtil.readDataFromFile("json/countries-orig.json");
        final String expectedBody = FileUtil.readDataFromFile("body/country-dtos-json-body.txt")
                .replaceAll("[\\r\\n\t]", "");

        stubFor(WireMock.get(urlEqualTo(GET_ALL_COUNTRIES_DATA_URL))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                        .withBody(countriesDataJson)));

        given().port(port)
                .header(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                .when()
                .get(GET_COUNTRIES_URL)
                .then().assertThat()
                .header(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(expectedBody));
    }

    @Test
    void shouldReturnCountryByName() throws IOException {
        final String countriesDataJson = FileUtil.readDataFromFile("json/countries-orig.json");
        final String expectedBody = FileUtil.readDataFromFile("body/country-detail-dto-json-body.txt")
                .replaceAll("[\\r\\n\t]", "");

        stubFor(WireMock.get(urlEqualTo(GET_ALL_COUNTRIES_DATA_URL))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                        .withBody(countriesDataJson)));

        given().port(port)
                .header(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                .when()
                .get(GET_VALID_COUNTRY_BY_NAME_URL)
                .then().assertThat()
                .header(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                .statusCode(HttpStatus.OK.value())
                .body(Matchers.equalTo(expectedBody));
    }

    @Test
    void shouldReturn404NotFound_whenCountryNotFoundByName() throws IOException {
        final String countriesDataJson = FileUtil.readDataFromFile("json/countries-orig.json");

        stubFor(WireMock.get(urlEqualTo(GET_ALL_COUNTRIES_DATA_URL))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                        .withBody(countriesDataJson)));

        given().port(port)
                .header(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                .when()
                .get(GET_INVALID_COUNTRY_BY_NAME_URL)
                .then().assertThat()
                .header(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(Matchers.equalTo("{\"errorMessage\":\"Country not found\"," +
                        "\"innerExceptionErrorMessage\":\"Country with name <Alban> not found\"}"));
    }


    @Test
    void shouldReturn400BadRequest_whenGetCountriesAndRetrievalCountryDataError() throws IOException {
        final String countriesDataJson = FileUtil.readDataFromFile("json/countries-orig-bad.json");

        stubFor(WireMock.get(urlEqualTo(GET_ALL_COUNTRIES_DATA_URL))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                        .withBody(countriesDataJson)));

        given().port(port)
                .header(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                .when()
                .get(GET_COUNTRIES_URL)
                .then().assertThat()
                .header(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(Matchers.containsString(
                        "{\"errorMessage\":\"Country list retrieval exception\",\"innerExceptionErrorMessage\":"));
    }

    @Test
    void shouldReturn400BadRequest_whenGetCountryByNameAndRetrievalCountryDataError() throws IOException {
        final String countriesDataJson = FileUtil.readDataFromFile("json/countries-orig-bad.json");

        stubFor(WireMock.get(urlEqualTo(GET_ALL_COUNTRIES_DATA_URL))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                        .withBody(countriesDataJson)));

        given().port(port)
                .header(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                .when()
                .get(GET_VALID_COUNTRY_BY_NAME_URL)
                .then().assertThat()
                .header(CONTENT_TYPE, APPLICATION_JSON_CHARSET)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(Matchers.containsString(
                        "{\"errorMessage\":\"Country list retrieval exception\",\"innerExceptionErrorMessage\":"));
    }
}
