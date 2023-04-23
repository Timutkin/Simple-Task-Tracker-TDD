package ru.timutkin.tdd.exception;

import ru.timutkin.tdd.web.handler.ApiValidationError;

public class TaskNotFoundException extends RuntimeException implements ValidationException{
    private final ApiValidationError validationError;

    public TaskNotFoundException(ApiValidationError error) {
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
