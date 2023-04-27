package ru.timutkin.tdd.exception;

import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;

public interface ApiException {
    String getMessage();
    ApiValidationError getValidationError();
}
