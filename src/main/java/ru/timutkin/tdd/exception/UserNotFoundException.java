package ru.timutkin.tdd.exception;

import ru.timutkin.tdd.web.handler.ApiValidationError;

public class UserNotFoundException extends RuntimeException implements NotFoundException {
    private final ApiValidationError validationError;

    public UserNotFoundException(ApiValidationError error) {
        this.validationError = error;
    }

    @Override
    public String getMessage() {
        return validationError.getMessage();
    }

    public ApiValidationError getValidationError() {
        return validationError;
    }
}
