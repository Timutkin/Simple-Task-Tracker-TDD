package ru.timutkin.tdd.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.service.TaskService;
import ru.timutkin.tdd.web.constant.ApiConstant;
import ru.timutkin.tdd.web.request.CreationTaskRequest;
import ru.timutkin.tdd.web.validation.TaskControllerValidation;

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
        TaskControllerValidation.validate(request);
        TaskDto task = taskService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(task);
    }

    @PutMapping
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto){
        TaskControllerValidation.validateUpdate(taskDto);
        TaskDto updatedTaskDto = taskService.update(taskDto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedTaskDto);
    }


    @GetMapping
    public ResponseEntity<List<TaskDto>> findAll(){
        List<TaskDto> taskDtoList = taskService.findAll();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskDtoList);
    }
}
