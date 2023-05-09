package ru.timutkin.tdd.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.dto.param.FilterTaskParams;
import ru.timutkin.tdd.exception.not_found.ProjectNotFoundException;
import ru.timutkin.tdd.store.entity.ProjectEntity;
import ru.timutkin.tdd.store.entity.TaskEntity;
import ru.timutkin.tdd.store.entity.UserEntity;
import ru.timutkin.tdd.exception.not_found.TaskNotFoundException;
import ru.timutkin.tdd.exception.not_found.UserNotFoundException;
import ru.timutkin.tdd.mapper.TaskMapper;
import ru.timutkin.tdd.store.repository.ProjectRepository;
import ru.timutkin.tdd.store.repository.TaskRepository;
import ru.timutkin.tdd.store.repository.UserRepository;
import ru.timutkin.tdd.store.repository.specifiction.TaskSpecification;
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

    private final ProjectRepository projectRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TaskDto save(CreationTaskRequest taskRequest) {
        TaskEntity taskEntity = new TaskEntity(taskRequest.getTaskName(), taskRequest.getMessage());
        taskEntity.setUser(
                userRepository.findById(taskRequest.getUserId()).orElseThrow(
                        () -> new UserNotFoundException(
                                ApiValidationError.getApiValidationError(taskRequest,
                                        ValidationConstant.USER_WITH_ID_NOT_FOUND.formatted(taskRequest.getUserId()),
                                        "userID", taskRequest.getUserId())
                        )));
        taskEntity.setProject(
                projectRepository.findById(taskRequest.getProjectId()).orElseThrow(
                        () -> new ProjectNotFoundException(
                                ApiValidationError.getApiValidationError(taskRequest,
                                        ValidationConstant.PROJECT_WITH_ID_NOT_FOUND.formatted(taskRequest.getProjectId()),
                                        "projectId", taskRequest.getProjectId()))
                )
        );
        taskRepository.saveAndFlush(taskEntity);
        return taskMapper.taskEntityToTaskDto(taskEntity);
    }


    @Override
    @Retryable(maxAttempts = 2)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
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
        if (taskDto.getProjectId() != null){
            ProjectEntity newProject = projectRepository.findById(taskDto.getProjectId()).orElseThrow(
                    () -> new ProjectNotFoundException(
                            ApiValidationError.getApiValidationError(taskDto,
                                    ValidationConstant.PROJECT_WITH_ID_NOT_FOUND.formatted(taskDto.getProjectId()),
                                    "projectId", taskDto.getProjectId()))
            );
            task.setProject(newProject);
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<TaskDto> findByParam(FilterTaskParams filterTaskParams) {
        return taskRepository.findAll(
                Specification.where(TaskSpecification.filterTasks(filterTaskParams))
        ).stream().map(taskMapper::taskEntityToTaskDto).toList();
    }

    @Override
    public List<TaskDto> findListOfTaskByUserId(Long userId) {
        return taskRepository.findTaskEntityByUserId(userId)
                .stream()
                .map(taskMapper::taskEntityToTaskDto)
                .toList();
    }

}
