package ru.timutkin.tdd.web.validation;

import lombok.experimental.UtilityClass;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.enumeration.Status;
import ru.timutkin.tdd.exception.IncorrectFieldException;

import ru.timutkin.tdd.exception.IncorrectPathVariableException;
import ru.timutkin.tdd.web.handler.ApiValidationError;
import ru.timutkin.tdd.web.request.CreationTaskRequest;

import static ru.timutkin.tdd.web.handler.ApiValidationError.*;

@UtilityClass
public class TaskControllerValidation {

    public static void validateUpdate(TaskDto taskDto) throws IncorrectFieldException {
        if (taskDto.getId() == null || taskDto.getId() <= 0) {
            String message = "The id should not be null or <= 0";
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "id", taskDto.getId())
            );
        }
        if (taskDto.getDataTimeOfCreation() != null && !DataValidation.validate(taskDto.getDataTimeOfCreation())){
            String message = "Correct format: yyyy-MM-dd HH:mm";
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "dataTimeOfCreation", taskDto.getDataTimeOfCreation())
            );
        }
        if (taskDto.getTaskName() != null && taskDto.getTaskName().isBlank()) {
            String message = "The task name should not be empty, consist of spaces";
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "taskName", taskDto.getTaskName())
            );
        }
        if (taskDto.getMessage() != null && taskDto.getMessage().isBlank()) {
            String message = "The message should not be empty, consist of spaces";
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "message", taskDto.getMessage())
            );
        }
        String status = taskDto.getStatus();
        if (status != null &&
                (!status.equals(Status.OPEN.toString()) && !status.equals(Status.IN_PROGRESS.toString()) &&
               ! status.equals(Status.REOPENED.toString()) && !status.equals(Status.RESOLVED.toString()) &&
                !status.equals(Status.CLOSED.toString())
        )) {
            String message = "Acceptable values: OPEN, IN_PROGRESS, RESOLVED, REOPENED, CLOSED";
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "status", taskDto.getStatus())
            );
        }
        if (taskDto.getUserId() == null || taskDto.getUserId() <= 0) {
            String message = "The user id should not be <= 0";
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "userId", taskDto.getUserId())
            );
        }
    }

    public static void validateCreate(CreationTaskRequest request) throws IncorrectFieldException{

        if (request.getTaskName() == null || request.getTaskName().isBlank()) {
            String message = "The task name should not be empty, consist of spaces or be null";
            throw new IncorrectFieldException(
                    getApiValidationError(request, message, "taskName", request.getTaskName())
            );
        }
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            String message = "The message should not be empty, consist of spaces or be null";
            throw new IncorrectFieldException(
                    getApiValidationError(request, message, "message", request.getMessage())
            );
        }
        if (request.getUserId() == null || request.getUserId() <= 0) {
            String message = "The user id should not be null or <= 0";
            throw new IncorrectFieldException(
                    getApiValidationError(request, message, "userId", request.getUserId())
            );
        }
    }

    public static void validatePathVariableId(Long id){
        if (id == null || id <=0){
            throw new IncorrectPathVariableException(ApiValidationError.builder()
                    .rejectedValue(id)
                    .message("The id should not be null or <= 0")
                    .field("/{taskId}")
                    .build());
        }
    }

}


