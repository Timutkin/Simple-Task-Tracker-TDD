package ru.timutkin.tdd.web.validation;

import lombok.experimental.UtilityClass;
import ru.timutkin.tdd.dto.DepartmentDto;
import ru.timutkin.tdd.exception.validation.IncorrectFieldException;
import ru.timutkin.tdd.exception.validation.IncorrectPathVariableException;
import ru.timutkin.tdd.exception.validation.IncorrectRequestParamException;
import ru.timutkin.tdd.web.constant.ValidationConstant;
import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;

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

    public static void validateId(Long departmentId) {
        if (departmentId == null || departmentId <= 0) {
            throw new IncorrectPathVariableException(ApiValidationError.builder()
                    .rejectedValue(departmentId)
                    .message(ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0)
                    .field("/{departmentId}")
                    .build());
        }
    }

    public static void validateUpdate(DepartmentDto departmentDto) {
        if (departmentDto.getId() == null || departmentDto.getId() <= 0) {
            String message = ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0;
            throw new IncorrectFieldException(
                    getApiValidationError(departmentDto, message, "id", departmentDto.getId())
            );
        }
        if (departmentDto.getName() != null && departmentDto.getName().isBlank()) {
            String message = ValidationConstant.THE_TASK_NAME_SHOULD_NOT_BE_EMPTY_CONSIST_OF_SPACES;
            throw new IncorrectFieldException(
                    getApiValidationError(departmentDto, message, "name", departmentDto.getName())
            );
        }
        if (departmentDto.getDepartmentHead() != null && departmentDto.getDepartmentHead() <= 0) {
            String message = ValidationConstant.THE_DEPARTMENT_ID_SHOULD_NOT_BE_LESS_OR_EQUAL_0;
            throw new IncorrectFieldException(
                    getApiValidationError(departmentDto, message, "projectId", departmentDto.getDepartmentHead())
            );
        }
    }
}
