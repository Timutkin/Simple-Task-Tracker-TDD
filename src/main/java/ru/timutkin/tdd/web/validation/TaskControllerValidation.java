package ru.timutkin.tdd.web.validation;

import lombok.experimental.UtilityClass;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.dto.param.FilterTaskParams;
import ru.timutkin.tdd.enumeration.Status;
import ru.timutkin.tdd.exception.validation.IncorrectFieldException;

import ru.timutkin.tdd.exception.validation.IncorrectPathVariableException;
import ru.timutkin.tdd.exception.validation.IncorrectRequestParamException;
import ru.timutkin.tdd.web.constant.ValidationConstant;
import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;
import ru.timutkin.tdd.dto.CreationTaskRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static ru.timutkin.tdd.web.handler.error_objects.ApiValidationError.*;

@UtilityClass
public class TaskControllerValidation {

    public static void validateUpdate(TaskDto taskDto) throws IncorrectFieldException {
        if (taskDto.getId() == null || taskDto.getId() <= 0) {
            String message = ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0;
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "id", taskDto.getId())
            );
        }
        if (taskDto.getCreatedAt() != null && !DataValidation.validate(taskDto.getCreatedAt())) {
            String message = ValidationConstant.CORRECT_DATA_FORMAT;
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "createdAt", taskDto.getCreatedAt())
            );
        }
        if (taskDto.getTaskName() != null && taskDto.getTaskName().isBlank()) {
            String message = ValidationConstant.THE_TASK_NAME_SHOULD_NOT_BE_EMPTY_CONSIST_OF_SPACES;
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "taskName", taskDto.getTaskName())
            );
        }
        if (taskDto.getMessage() != null && taskDto.getMessage().isBlank()) {
            String message = ValidationConstant.THE_MESSAGE_SHOULD_NOT_BE_EMPTY_CONSIST_OF_SPACES;
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "message", taskDto.getMessage())
            );
        }
        String status = taskDto.getStatus();
        if (status != null &&
            !(status.equals(Status.OPEN.toString()) || status.equals(Status.IN_PROGRESS.toString()) ||
              status.equals(Status.REOPENED.toString()) || status.equals(Status.RESOLVED.toString()) ||
              status.equals(Status.CLOSED.toString())
            )) {
            String message = ValidationConstant.VALUES_OPEN_IN_PROGRESS_RESOLVED_REOPENED_CLOSED;
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "status", taskDto.getStatus())
            );
        }
        if (taskDto.getUserId() != null && taskDto.getUserId() <= 0) {
            String message = ValidationConstant.THE_USER_ID_SHOULD_NOT_BE_LESS_OR_EQUAL_0;
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "userId", taskDto.getUserId())
            );
        }
        if (taskDto.getProjectId() != null && taskDto.getProjectId() <= 0) {
            String message = ValidationConstant.THE_PROJECT_ID_SHOULD_NOT_BE_LESS_OR_EQUAL_0;
            throw new IncorrectFieldException(
                    getApiValidationError(taskDto, message, "projectId", taskDto.getProjectId())
            );
        }

    }

    public static void validateCreate(CreationTaskRequest request) throws IncorrectFieldException {
        if (request.getTaskName() == null || request.getTaskName().isBlank()) {
            String message = ValidationConstant.THE_TASK_NAME_SHOULD_NOT_BE_EMPTY_CONSIST_OF_SPACES;
            throw new IncorrectFieldException(
                    getApiValidationError(request, message, "taskName", request.getTaskName())
            );
        }
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            String message = ValidationConstant.THE_MESSAGE_SHOULD_NOT_BE_EMPTY_CONSIST_OF_SPACES;
            throw new IncorrectFieldException(
                    getApiValidationError(request, message, "message", request.getMessage())
            );
        }
        if (request.getUserId() == null || request.getUserId() <= 0) {
            String message = ValidationConstant.THE_USER_ID_SHOULD_NOT_BE_LESS_OR_EQUAL_0;
            throw new IncorrectFieldException(
                    getApiValidationError(request, message, "userId", request.getUserId())
            );
        }
        if (request.getProjectId() == null || request.getProjectId() <= 0) {
            String message = ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0;
            throw new IncorrectFieldException(
                    getApiValidationError(request, message, "projectId", request.getProjectId())
            );
        }
    }

    public static void validatePathVariableAndRequestParamId(Long id) {
        if (id == null || id <= 0) {
            throw new IncorrectPathVariableException(ApiValidationError.builder()
                    .rejectedValue(id)
                    .message(ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0)
                    .field("/{taskId}")
                    .build());
        }
    }

    public static void validateFilter(FilterTaskParams filterTaskParams) {

        filterTaskParams.after().ifPresent(localDateTime -> {
            String exMessage = ValidationConstant.CORRECT_DATA_FORMAT;
            if (!DataValidation.validate(localDateTime)) {
                throw new IncorrectRequestParamException(getApiValidationError(localDateTime, exMessage, "after", localDateTime));
            }
        });
        filterTaskParams.before().ifPresent(localDateTime -> {
            String exMessage = ValidationConstant.CORRECT_DATA_FORMAT;
            if (!DataValidation.validate(localDateTime)) {
                throw new IncorrectRequestParamException(getApiValidationError(localDateTime, exMessage, "before", localDateTime));
            }
        });
        filterTaskParams.taskName().ifPresent(name -> {
            String exMessage = ValidationConstant.THE_TASK_NAME_SHOULD_NOT_BE_EMPTY_CONSIST_OF_SPACES;
            if (name.isBlank()) {
                throw new IncorrectRequestParamException(getApiValidationError(name, exMessage, "taskName", name));
            }
        });
        filterTaskParams.message().ifPresent(mess -> {
            String exMessage = ValidationConstant.THE_MESSAGE_SHOULD_NOT_BE_EMPTY_CONSIST_OF_SPACES;
            if (mess.isBlank()) {
                throw new IncorrectRequestParamException(getApiValidationError(mess, exMessage, "message", mess));
            }
        });
        filterTaskParams.status().ifPresent(stat -> {
            String exMessage = ValidationConstant.VALUES_OPEN_IN_PROGRESS_RESOLVED_REOPENED_CLOSED;
            if (!(stat.equals(Status.OPEN.toString()) || stat.equals(Status.IN_PROGRESS.toString()) ||
                  stat.equals(Status.REOPENED.toString()) || stat.equals(Status.RESOLVED.toString()) ||
                  stat.equals(Status.CLOSED.toString()))) {
                throw new IncorrectRequestParamException(getApiValidationError(stat, exMessage, "status", stat));
            }
        });
        filterTaskParams.userId().ifPresent(id -> {
            String exMessage = ValidationConstant.THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0;
            if (id <= 0) {
                throw new IncorrectRequestParamException(getApiValidationError(id, exMessage, "userId", id));
            }
        });
        filterTaskParams.projectId().ifPresent(id -> {
            String exMessage = ValidationConstant.THE_PROJECT_ID_SHOULD_NOT_BE_LESS_OR_EQUAL_0;
            if (id <= 0) {
                throw new IncorrectRequestParamException(getApiValidationError(id, exMessage, "projectId", id));
            }
        });

    }
}


