package com.assignment.countryservice.web;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "Generic error response class")
public class GenericErrorResponse {

    @Schema(description = "Human readable error message")
    private String errorMessage;

    @Schema(description = "Thrown inner exception error message")
    private String innerExceptionErrorMessage;
}
