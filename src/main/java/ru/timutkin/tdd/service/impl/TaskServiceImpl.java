package ru.timutkin.tdd.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
import ru.timutkin.tdd.repository.specifiction.TaskSpecification;
import ru.timutkin.tdd.service.TaskService;
import ru.timutkin.tdd.web.constant.ValidationConstant;
import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;
import ru.timutkin.tdd.dto.CreationTaskRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


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
                                        ValidationConstant.USER_WITH_ID_NOT_FOUND.formatted(taskRequest.getUserId()),
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
                                ValidationConstant.TASK_WITH_ID_NOT_FOUND.formatted(taskDto.getId()),
                                "id", taskDto.getId())
                )
        );
        if (taskDto.getUserId() != null){
            UserEntity newUser = userRepository.findById(taskDto.getUserId()).orElseThrow(
                    () -> new UserNotFoundException(
                            ApiValidationError.getApiValidationError(taskDto,
                                    ValidationConstant.USER_WITH_ID_NOT_FOUND.formatted(taskDto.getUserId()),
                                    "userID", taskDto.getUserId()))
            );
            task.setUser(newUser);
        }
        taskMapper.updateTaskEntityFromTaskDto(taskDto, task);
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
                        .message(ValidationConstant.TASK_WITH_ID_NOT_FOUND.formatted(taskId))
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
                                .message(ValidationConstant.TASK_WITH_ID_NOT_FOUND.formatted(taskId))
                                .field("/{taskId}")
                                .build())
                )
        );
    }

    @Override
    public List<TaskDto> findByParam(Optional<LocalDateTime> after, Optional<LocalDateTime> before,
                                     Optional<String> taskName, Optional<String> message,
                                     Optional<String> status, Optional<Long> userId) {
        return taskRepository.findAll(
                Specification.where(TaskSpecification.filterTasks(after, before, taskName, message, status, userId))
        ).stream().map(taskMapper::taskEntityToTaskDto).toList();
    }

}
