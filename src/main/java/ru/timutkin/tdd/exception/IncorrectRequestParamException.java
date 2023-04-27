package ru.timutkin.tdd.exception;

import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;

public class IncorrectRequestParamException extends RuntimeException implements ValidationException{

    private final ApiValidationError validationError;

    public IncorrectRequestParamException(ApiValidationError validationError){
        this.validationError = validationError;
    }

    @Override
    public ApiValidationError getValidationError() {
        return validationError;
    }

    @Override
    public String getMessage() {
        return validationError.getMessage();
    }
}
