package ru.timutkin.tdd.exception;


import ru.timutkin.tdd.web.handler.ApiValidationError;

public class IncorrectDataException extends RuntimeException {
    private final ApiValidationError validationError;


    public IncorrectDataException(ApiValidationError error) {
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
