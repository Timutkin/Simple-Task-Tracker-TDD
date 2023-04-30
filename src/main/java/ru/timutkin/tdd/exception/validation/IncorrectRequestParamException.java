package ru.timutkin.tdd.exception.validation;

import ru.timutkin.tdd.exception.BaseApiException;
import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;

public class IncorrectRequestParamException extends BaseApiException implements ValidationException {

    public IncorrectRequestParamException(ApiValidationError apiValidationError) {
        super(apiValidationError);
    }
}
