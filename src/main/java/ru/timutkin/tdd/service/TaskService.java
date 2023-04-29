package ru.timutkin.tdd.service;


import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.dto.CreationTaskRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskDto save(CreationTaskRequest taskRequest);

    TaskDto update(TaskDto taskDto);

    void deleteById(Long taskId);

    TaskDto findById(Long taskId);

    List<TaskDto> findByParam(Optional<LocalDateTime> after, Optional<LocalDateTime> before,
                              Optional<String> taskName, Optional<String> message,
                              Optional<String> status, Optional<Long> userId, Optional<Long> projectId);

    List<TaskDto> findListOfTaskByUserId(Long userId);
}
