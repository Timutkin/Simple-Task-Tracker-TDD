package ru.timutkin.tdd.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.entity.TaskEntity;
import ru.timutkin.tdd.entity.UserEntity;
import ru.timutkin.tdd.exception.TaskNotFoundException;
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

    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public TaskDto save(CreationTaskRequest taskRequest) {
        TaskEntity taskEntity = new TaskEntity(taskRequest.getTaskName(), taskRequest.getMessage());
        taskEntity.setUser(
                userRepository.findById(taskRequest.getUserId()).orElseThrow(
                        () -> new UserNotFoundException(
                                ApiValidationError.getApiValidationError(taskRequest,
                                        "User with id = " + taskRequest.getUserId() + " not found",
                                        "userID", taskRequest.getUserId())
                        )));
        taskRepository.saveAndFlush(taskEntity);
        return taskMapper.taskEntityToTaskDto(taskEntity);
    }


    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public List<TaskDto> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::taskEntityToTaskDto)
                .toList();
    }

    @Override
    @Transactional
    public TaskDto update(TaskDto taskDto) {
        TaskEntity task = taskRepository.findById(taskDto.getId()).orElseThrow(
                () -> new TaskNotFoundException(
                        ApiValidationError.getApiValidationError(taskDto,
                                "Task with id = " + taskDto.getId() + " not found",
                                "id", taskDto.getId())
                )
        );
        UserEntity newUser = userRepository.findById(taskDto.getUserId()).orElseThrow(
                () -> new UserNotFoundException(
                        ApiValidationError.getApiValidationError(taskDto,
                                "User with id = " + taskDto.getUserId() + " not found",
                                "userID", taskDto.getUserId()))
        );
        taskMapper.updateTaskEntityFromTaskDto(taskDto, task);
        task.setUser(newUser);
        taskRepository.saveAndFlush(task);
        return taskMapper.taskEntityToTaskDto(task);
    }

    @Override
    @Retryable(maxAttempts = 2)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteById(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
                () -> new TaskNotFoundException(ApiValidationError.builder()
                        .rejectedValue(taskId)
                        .message("Task with id = " + taskId + " not found")
                        .field("/{taskId}")
                        .build())
        );
        taskRepository.deleteById(task.getId());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TaskDto findById(Long taskId) {
        return taskMapper.taskEntityToTaskDto(
                taskRepository.findById(taskId).orElseThrow(
                        () -> new TaskNotFoundException(ApiValidationError.builder()
                                .rejectedValue(taskId)
                                .message("Task with id = " + taskId + " not found")
                                .field("/{taskId}")
                                .build())
                )
        );
    }

}
