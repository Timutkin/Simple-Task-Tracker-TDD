package ru.timutkin.tdd.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.exception.IncorrectDataException;
import ru.timutkin.tdd.service.TaskService;
import ru.timutkin.tdd.web.constant.ApiConstant;
import ru.timutkin.tdd.web.handler.ApiValidationError;
import ru.timutkin.tdd.web.request.CreationTaskRequest;

@RestController
@AllArgsConstructor
@RequestMapping(
        value =    ApiConstant.VERSION_API+ "/tasks",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class TaskController {

    TaskService taskService;

    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody CreationTaskRequest request) {
        if (request.getTaskName() == null || request.getTaskName().isBlank()) {
            throw new IncorrectDataException(
                    ApiValidationError.builder()
                            .message("The task name should not be empty, consist of spaces or be null")
                            .object(request.getClass().getSimpleName())
                            .rejectedValue(request.getTaskName())
                            .field("taskName")
                            .build()
            );
        }
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            throw new IncorrectDataException(
                    ApiValidationError.builder()
                            .message("The message should not be empty, consist of spaces or be null")
                            .object(request.getClass().getSimpleName())
                            .rejectedValue(request.getMessage())
                            .field("message")
                            .build()
            );
        }
        if (request.getUserId() == null || request.getUserId() <= 0) {
            throw new IncorrectDataException(
                    ApiValidationError.builder()
                            .message("The user id should not be null or <= 0 ")
                            .object(request.getClass().getSimpleName())
                            .rejectedValue(request.getUserId())
                            .field("userId")
                            .build()
            );
        }
        TaskDto task = taskService.save(request);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(task);
    }
}
