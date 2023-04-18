package ru.timutkin.tdd.service.impl;

import org.springframework.stereotype.Service;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.service.TaskService;
import ru.timutkin.tdd.web.request.CreationTaskRequest;

@Service
public class TaskServiceImpl implements TaskService {
    @Override
    public TaskDto save(CreationTaskRequest taskRequest) {
        return null;
    }
}
