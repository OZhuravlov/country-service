package com.assignment.countryservice.web.handler;

import com.assignment.countryservice.exception.CountryNotFoundException;
import com.assignment.countryservice.exception.CountryRetrievalException;
import com.assignment.countryservice.web.GenericErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GenericExceptionHandler {

    private static final String COUNTRY_LIST_RETRIEVAL_ERROR_EVENT = "Country list retrieval exception";
    private static final String COUNTRY_NOT_FOUND_EVENT = "Country not found";
    private static final String COUNTRY_UNKNOWN_ERROR_EVENT = "Unknown error";

    @ExceptionHandler(CountryRetrievalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<GenericErrorResponse> handleCountryRetrievalException(CountryRetrievalException ex) {
        log.warn("event={}", COUNTRY_LIST_RETRIEVAL_ERROR_EVENT, ex);
        return buildEntityResponse(ex, COUNTRY_LIST_RETRIEVAL_ERROR_EVENT);
    }

    @ExceptionHandler(CountryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<GenericErrorResponse> handleCountryNotFoundException(final CountryNotFoundException ex) {
        log.warn("event={}", COUNTRY_NOT_FOUND_EVENT, ex);
        return buildEntityResponse(ex, COUNTRY_NOT_FOUND_EVENT);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<GenericErrorResponse> handleUnknownException(final Exception ex) {
        log.error("event={}", COUNTRY_UNKNOWN_ERROR_EVENT, ex);
        return buildEntityResponse(ex, COUNTRY_UNKNOWN_ERROR_EVENT);
    }

    private Mono<GenericErrorResponse> buildEntityResponse(final Exception innerException,
                                                           final String eventName) {
        return Mono.just(GenericErrorResponse.builder()
                .errorMessage(eventName)
                .innerExceptionErrorMessage(innerException.getMessage())
                .build());
    }
}
