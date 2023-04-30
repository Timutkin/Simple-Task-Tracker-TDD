package ru.timutkin.tdd.exception.not_found;

import ru.timutkin.tdd.exception.BaseApiException;
import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;

public class ProjectNotFoundException  extends BaseApiException implements NotFoundException {

    public ProjectNotFoundException(ApiValidationError apiValidationError) {
        super(apiValidationError);
    }
}
