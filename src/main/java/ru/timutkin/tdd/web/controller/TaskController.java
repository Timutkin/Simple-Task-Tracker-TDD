package ru.timutkin.tdd.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.service.TaskService;
import ru.timutkin.tdd.web.constant.ApiConstant;
import ru.timutkin.tdd.web.request.CreationTaskRequest;

@AllArgsConstructor
@RequestMapping(
        value = ApiConstant.VERSION_API+"/tasks",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class TaskController {

    TaskService taskService;

    @PostMapping()
    public ResponseEntity<TaskDto> createTask(@RequestBody CreationTaskRequest request){
        return null;
    }
}
