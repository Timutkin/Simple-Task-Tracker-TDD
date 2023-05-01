package ru.timutkin.tdd.service.impl;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.tdd.dto.ProjectDto;
import ru.timutkin.tdd.exception.not_found.ProjectNotFoundException;
import ru.timutkin.tdd.exception.not_found.TaskNotFoundException;
import ru.timutkin.tdd.exception.not_found.UserNotFoundException;
import ru.timutkin.tdd.mapper.ProjectMapper;
import ru.timutkin.tdd.service.ProjectService;
import ru.timutkin.tdd.store.entity.ProjectEntity;
import ru.timutkin.tdd.store.entity.TaskEntity;
import ru.timutkin.tdd.store.entity.UserEntity;
import ru.timutkin.tdd.store.repository.ProjectRepository;
import ru.timutkin.tdd.store.repository.TaskRepository;
import ru.timutkin.tdd.store.repository.UserRepository;
import ru.timutkin.tdd.web.constant.ValidationConstant;
import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.timutkin.tdd.web.handler.error_objects.ApiValidationError.getApiValidationError;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;

    UserRepository userRepository;

    TaskRepository taskRepository;

    ProjectMapper projectMapper;

    EntityManager entityManager;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ProjectDto create(String name, Long userHead, List<Long> tasksId) {
        ProjectEntity project = new ProjectEntity();
        UserEntity user = null;
        List<TaskEntity> taskEntityList = new ArrayList<>();
        if (userHead != null) {
            user = userRepository.findById(userHead).orElseThrow(
                    () -> new UserNotFoundException(
                            getApiValidationError(userHead,
                                    ValidationConstant.USER_WITH_ID_NOT_FOUND.formatted(userHead), "headId", userHead)
                    )
            );
        }
        if (tasksId != null) {
            tasksId.forEach( taskId -> {
                        var task  = taskRepository.findById(taskId).orElseThrow(
                                () -> new TaskNotFoundException(ApiValidationError.builder()
                                        .rejectedValue(taskId)
                                        .message(ValidationConstant.TASK_WITH_ID_NOT_FOUND.formatted(taskId))
                                        .field("tasksId")
                                        .build())
                        );
                        taskEntityList.add(task);
                    }
            );

        }
        project.setUserHead(user);
        project.setTaskEntityList(taskEntityList);
        project.setName(name);
        projectRepository.save(project);
        ProjectDto projectDto = projectMapper.projectEntityToProjectDto(project);
        projectDto.setTasksId(project.getTaskEntityList().stream().map(TaskEntity::getId).toList());
        return projectDto;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ProjectDto findById(Long projectId) {
        EntityGraph<ProjectEntity> entityGraph = entityManager.createEntityGraph(ProjectEntity.class);
        entityGraph.addAttributeNodes("taskEntityList","userHead");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);
        ProjectEntity project = entityManager.find(ProjectEntity.class, projectId,properties);
        if (project == null){
            throw new ProjectNotFoundException(
                    ApiValidationError.getApiValidationError(projectId,
                            ValidationConstant.PROJECT_WITH_ID_NOT_FOUND.formatted(projectId),
                            "projectId", projectId));
        }
        ProjectDto projectDto = projectMapper.projectEntityToProjectDto(project);
        projectDto.setTasksId(project.getTaskEntityList().stream().map(TaskEntity::getId).toList());
        return projectDto;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteById(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException(
                ApiValidationError.getApiValidationError(projectId,
                        ValidationConstant.PROJECT_WITH_ID_NOT_FOUND.formatted(projectId),
                        "projectId", projectId))
        );
        projectRepository.delete(project);
    }
}
