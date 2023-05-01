package ru.timutkin.tdd.web.validation;

import lombok.experimental.UtilityClass;
import ru.timutkin.tdd.exception.validation.IncorrectPathVariableException;
import ru.timutkin.tdd.exception.validation.IncorrectRequestParamException;
import ru.timutkin.tdd.web.constant.ValidationConstant;
import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;

import java.util.List;

import static ru.timutkin.tdd.web.handler.error_objects.ApiValidationError.getApiValidationError;

@UtilityClass
public class ProjectControllerValidation {
    public static void validateCreate(String name, Long userHead, List<Long> tasksId) {
        if (name == null || name.isBlank()) {
            String message = ValidationConstant.THE_PROJECT_NAME_NOT_BE_EMPTY_CONSIST_OF_SPACES;
            throw new IncorrectRequestParamException(
                    getApiValidationError(name, message, "name", name)
            );
        }
        if (userHead != null && userHead <= 0) {
            String message = ValidationConstant.THE_ID_SHOULD_NOT_BE_LESS_OR_EQUAL_0;
            throw new IncorrectRequestParamException(
                    getApiValidationError(userHead, message, "userHead", userHead)
            );
        }
        if (tasksId != null) {
            tasksId.forEach(taskId -> {
                if (taskId <= 0) {
                    String message = ValidationConstant.THE_ID_SHOULD_NOT_BE_LESS_OR_EQUAL_0;
                    throw new IncorrectRequestParamException(
                            getApiValidationError(taskId, message, "tasksId", taskId)
                    );
                }
            });
        }
    }

    public static void validateId(Long projectId) {
        if (projectId == null || projectId <= 0) {
            throw new IncorrectPathVariableException(ApiValidationError.builder()
                    .rejectedValue(projectId)
                    .message(ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0)
                    .field("/{projectId}")
                    .build());
        }
    }
}
