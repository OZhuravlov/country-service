package com.assignment.countryservice.exception;

public class CountryRetrievalException extends RuntimeException {
    public CountryRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }
}
