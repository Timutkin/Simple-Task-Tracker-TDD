package ru.timutkin.tdd.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.entity.TaskEntity;
import ru.timutkin.tdd.exception.UserNotFoundException;
import ru.timutkin.tdd.mapper.TaskMapper;
import ru.timutkin.tdd.repository.TaskRepository;
import ru.timutkin.tdd.repository.UserRepository;
import ru.timutkin.tdd.service.TaskService;
import ru.timutkin.tdd.web.handler.ApiValidationError;
import ru.timutkin.tdd.web.request.CreationTaskRequest;

import java.util.List;


@AllArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final TaskMapper mapper;

    @Override
    @Transactional
    public TaskDto save(CreationTaskRequest taskRequest) {
        TaskEntity taskEntity = new TaskEntity(taskRequest.getTaskName(), taskRequest.getMessage());
        taskEntity.setUser(
                userRepository.findById(taskRequest.getUserId()).orElseThrow(
                        ()-> new UserNotFoundException(
                                ApiValidationError.builder()
                                        .object(taskRequest.getClass().getSimpleName())
                                        .field("userID")
                                        .rejectedValue(taskRequest.getUserId())
                                        .message("User with id = " + taskRequest.getUserId() + " not found")
                                        .build())
                        )
        );
        taskRepository.saveAndFlush(taskEntity);
        return mapper.taskEntityToTaskDto(taskEntity);
    }


    @Transactional
    public List<TaskDto> findAll(){
        return taskRepository.findAll()
                .stream()
                .map(mapper::taskEntityToTaskDto)
                .toList();
    }
}
