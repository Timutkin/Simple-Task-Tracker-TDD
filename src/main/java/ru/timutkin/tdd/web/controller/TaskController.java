package ru.timutkin.tdd.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.service.TaskService;
import ru.timutkin.tdd.web.constant.ApiConstant;
import ru.timutkin.tdd.dto.CreationTaskRequest;
import ru.timutkin.tdd.web.validation.TaskControllerValidation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(
        value = ApiConstant.VERSION_API + "/tasks",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class TaskController {

    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody CreationTaskRequest request) {
        TaskControllerValidation.validateCreate(request);
        TaskDto task = taskService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(task);
    }

    @PutMapping
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto) {
        TaskControllerValidation.validateUpdate(taskDto);
        TaskDto updatedTaskDto = taskService.update(taskDto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedTaskDto);
    }


    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> findById(@PathVariable Long taskId) {
        TaskControllerValidation.validatePathVariableId(taskId);
        TaskDto task = taskService.findById(taskId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(task);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> findAllByParam(@RequestParam(name = "after") Optional<LocalDateTime> after,
                                                        @RequestParam(name = "before") Optional<LocalDateTime> before,
                                                        @RequestParam(name = "taskName") Optional<String> taskName,
                                                        @RequestParam(name = "message") Optional<String> message,
                                                        @RequestParam(name = "status") Optional<String> status,
                                                        @RequestParam(name = "userId") Optional<Long> userId

    ) {
        TaskControllerValidation.validateFilter(after, before, taskName, message, status, userId);
        List<TaskDto> taskDtoList = taskService.findByParam(after, before, taskName, message, status, userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskDtoList);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Long> deleteById(@PathVariable(required = false) Long taskId) {
        TaskControllerValidation.validatePathVariableId(taskId);
        taskService.deleteById(taskId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskId);
    }
}
