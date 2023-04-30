package ru.timutkin.tdd.web.validation;

import lombok.experimental.UtilityClass;
import ru.timutkin.tdd.exception.validation.IncorrectRequestParamException;
import ru.timutkin.tdd.web.constant.ValidationConstant;

import static ru.timutkin.tdd.web.handler.error_objects.ApiValidationError.getApiValidationError;

@UtilityClass
public class DepartmentControllerValidation {


    public static void validateCreate(String name, Long headId) {
        if (name == null || name.isBlank()){
            String message = ValidationConstant.THE_TASK_NAME_SHOULD_NOT_BE_EMPTY_CONSIST_OF_SPACES;
            throw new IncorrectRequestParamException(
                    getApiValidationError(name, message, "name",name)
            );
        }
        if (headId == null || headId <= 0){
            String message = ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0;
            throw new IncorrectRequestParamException(
                    getApiValidationError(headId, message, "headId",headId)
            );
        }
    }
}
