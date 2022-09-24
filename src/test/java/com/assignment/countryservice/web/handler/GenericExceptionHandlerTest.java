package com.assignment.countryservice.web.handler;

import com.assignment.countryservice.exception.CountryNotFoundException;
import com.assignment.countryservice.exception.CountryRetrievalException;
import com.assignment.countryservice.web.GenericErrorResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class GenericExceptionHandlerTest {

    private static final String COUNTRY_LIST_RETRIEVAL_ERROR_EVENT = "Country list retrieval exception";
    private static final String COUNTRY_NOT_FOUND_EVENT = "Country not found";
    private static final String COUNTRY_UNKNOWN_ERROR_EVENT = "Unknown error";

    private final GenericExceptionHandler genericControllerErrorHandler = new GenericExceptionHandler();

    @Test
    void shouldReturnGenericErrorResponse_whenHandleUnknownException() {
        // setup
        final String innerErrorMessage = "Unknown";
        final RuntimeException unknownException = new RuntimeException(innerErrorMessage);

        // execute
        final Mono<GenericErrorResponse> response
                = genericControllerErrorHandler.handleUnknownException(unknownException);

        // verify
        StepVerifier.create(response)
                .expectNextMatches(r -> innerErrorMessage.equals(r.getInnerExceptionErrorMessage())
                        && COUNTRY_UNKNOWN_ERROR_EVENT.equals(r.getErrorMessage()))
                .verifyComplete();
    }

    @Test
    void shouldReturnGenericErrorResponse_whenHandleCountryRetrievalException() {
        // setup
        final String innerErrorMessage = "retrieval error";
        final CountryRetrievalException exception = new CountryRetrievalException(innerErrorMessage, new Throwable());

        // execute
        final Mono<GenericErrorResponse> response
                = genericControllerErrorHandler.handleCountryRetrievalException(exception);

        // verify
        StepVerifier.create(response)
                .expectNextMatches(r -> innerErrorMessage.equals(r.getInnerExceptionErrorMessage())
                        && COUNTRY_LIST_RETRIEVAL_ERROR_EVENT.equals(r.getErrorMessage()))
                .verifyComplete();
    }

    @Test
    void shouldReturnGenericErrorResponse_whenHandleCountryNotFoundException() {
        // setup
        final String innerErrorMessage = "country not found";
        final CountryNotFoundException exception = new CountryNotFoundException(innerErrorMessage);

        // execute
        final Mono<GenericErrorResponse> response
                = genericControllerErrorHandler.handleCountryNotFoundException(exception);

        // verify
        StepVerifier.create(response)
                .expectNextMatches(r -> innerErrorMessage.equals(r.getInnerExceptionErrorMessage())
                        && COUNTRY_NOT_FOUND_EVENT.equals(r.getErrorMessage()))
                .verifyComplete();
    }
}
