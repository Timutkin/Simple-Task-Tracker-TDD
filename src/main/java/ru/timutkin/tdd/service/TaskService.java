package ru.timutkin.tdd.service;


import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.web.request.CreationTaskRequest;

import java.util.List;

public interface TaskService {
    TaskDto save(CreationTaskRequest taskRequest);

    List<TaskDto> findAll();

    TaskDto update(TaskDto taskDto);

    void deleteById(Long taskId);
}
