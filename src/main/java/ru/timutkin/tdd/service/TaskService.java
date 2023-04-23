package ru.timutkin.tdd.service;

import org.springframework.http.ResponseEntity;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.web.request.CreationTaskRequest;

import java.util.List;

public interface TaskService {
    TaskDto save(CreationTaskRequest taskRequest);

    List<TaskDto> findAll();

    TaskDto update(TaskDto taskDto);
}
