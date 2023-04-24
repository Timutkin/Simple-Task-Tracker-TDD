package ru.timutkin.tdd.exception;

import ru.timutkin.tdd.web.handler.ApiValidationError;

public class IncorrectPathVariableException extends RuntimeException implements ValidationException{

    private final ApiValidationError validationError;


    public IncorrectPathVariableException(ApiValidationError error) {
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