package ru.timutkin.tdd.web.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.exception.IncorrectFieldException;
import ru.timutkin.tdd.service.TaskService;
import ru.timutkin.tdd.web.constant.ApiConstant;
import ru.timutkin.tdd.web.handler.ApiValidationError;
import ru.timutkin.tdd.web.request.CreationTaskRequest;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(
        value =    ApiConstant.VERSION_API+ "/tasks",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class TaskController {

    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody CreationTaskRequest request) {
        if (request.getTaskName() == null || request.getTaskName().isBlank()) {
            throw new IncorrectFieldException(
                    ApiValidationError.builder()
                            .message("The task name should not be empty, consist of spaces or be null")
                            .object(request.getClass().getSimpleName())
                            .rejectedValue(request.getTaskName())
                            .field("taskName")
                            .build()
            );
        }
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            throw new IncorrectFieldException(
                    ApiValidationError.builder()
                            .message("The message should not be empty, consist of spaces or be null")
                            .object(request.getClass().getSimpleName())
                            .rejectedValue(request.getMessage())
                            .field("message")
                            .build()
            );
        }
        if (request.getUserId() == null || request.getUserId() <= 0) {
            throw new IncorrectFieldException(
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


    @GetMapping
    public ResponseEntity<List<TaskDto>> findAll(){
        List<TaskDto> taskDtoList = taskService.findAll();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskDtoList);
    }
}
