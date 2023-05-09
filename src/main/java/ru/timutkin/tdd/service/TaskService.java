package ru.timutkin.tdd.service;


import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.dto.CreationTaskRequest;
import ru.timutkin.tdd.dto.param.FilterTaskParams;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskDto save(CreationTaskRequest taskRequest);

    TaskDto update(TaskDto taskDto);

    void deleteById(Long taskId);

    TaskDto findById(Long taskId);

    List<TaskDto> findByParam(FilterTaskParams filterTaskParams);

    List<TaskDto> findListOfTaskByUserId(Long userId);
}
