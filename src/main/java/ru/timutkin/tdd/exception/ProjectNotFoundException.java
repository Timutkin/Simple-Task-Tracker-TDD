package ru.timutkin.tdd.exception;

import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;

public class ProjectNotFoundException  extends RuntimeException implements NotFoundException{
    private final ApiValidationError validationError;

    public ProjectNotFoundException(ApiValidationError error) {
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
