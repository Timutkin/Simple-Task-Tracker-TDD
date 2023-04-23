package ru.timutkin.tdd.exception;

import ru.timutkin.tdd.web.handler.ApiValidationError;

public interface ValidationException {
     String getMessage();
     ApiValidationError getValidationError();
}
