package ru.timutkin.tdd.exception;

import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;

public class  BaseApiException extends RuntimeException implements ApiException {

    private final ApiValidationError apiValidationError;

    public BaseApiException(ApiValidationError apiValidationError) {
        this.apiValidationError = apiValidationError;
    }

    @Override
    public ApiValidationError getValidationError() {
        return apiValidationError;
    }
}
