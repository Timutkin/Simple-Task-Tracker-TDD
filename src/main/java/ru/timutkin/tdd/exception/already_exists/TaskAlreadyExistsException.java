package ru.timutkin.tdd.exception.already_exists;

import ru.timutkin.tdd.exception.BaseApiException;
import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;

public class TaskAlreadyExistsException extends BaseApiException implements AlreadyExistsException{
    public TaskAlreadyExistsException(ApiValidationError apiValidationError) {
        super(apiValidationError);
    }
}
