package ru.timutkin.tdd.exception.already_exists;

import ru.timutkin.tdd.exception.BaseApiException;
import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;

public class ProjectAlreadyExistsException extends BaseApiException implements AlreadyExistsException {
    public ProjectAlreadyExistsException(ApiValidationError apiValidationError) {
        super(apiValidationError);
    }
}
