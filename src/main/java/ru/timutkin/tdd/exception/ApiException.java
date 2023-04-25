package ru.timutkin.tdd.exception;

import ru.timutkin.tdd.web.handler.ApiValidationError;

public interface ApiException {
    String getMessage();
    ApiValidationError getValidationError();
}
