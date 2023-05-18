package ru.timutkin.tdd.service.impl;

import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.timutkin.tdd.dto.ProjectDto;
import ru.timutkin.tdd.exception.already_exists.ProjectAlreadyExistsException;
import ru.timutkin.tdd.exception.not_found.ProjectNotFoundException;
import ru.timutkin.tdd.exception.not_found.UserNotFoundException;
import ru.timutkin.tdd.mapper.ProjectMapper;
import ru.timutkin.tdd.service.ProjectService;
import ru.timutkin.tdd.store.entity.ProjectEntity;
import ru.timutkin.tdd.store.entity.TaskEntity;
import ru.timutkin.tdd.store.entity.UserEntity;
import ru.timutkin.tdd.store.entity.graph.ProjectEntityGraph;
import ru.timutkin.tdd.store.repository.ProjectRepository;
import ru.timutkin.tdd.store.repository.UserRepository;
import ru.timutkin.tdd.web.constant.ValidationConstant;
import ru.timutkin.tdd.web.handler.error_objects.ApiValidationError;

import static ru.timutkin.tdd.web.handler.error_objects.ApiValidationError.getApiValidationError;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;

    ProjectEntityGraph projectEntityGraph;

    EntityManager entityManager;

    UserRepository userRepository;

    ProjectMapper projectMapper;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ProjectDto create(String name, Long userHead) {
        if (!projectRepository.existsByName(name)){
            throw new ProjectAlreadyExistsException(
                    getApiValidationError(name,
                            ValidationConstant.THE_PROJECT_WITH_NAME_ALREADY_EXIST.formatted(name), "name", name)
            );
        }
        ProjectEntity project = new ProjectEntity();
        UserEntity user = null;
        if (userHead != null) {
            user = userRepository.findById(userHead).orElseThrow(
                    () -> new UserNotFoundException(
                            getApiValidationError(userHead,
                                    ValidationConstant.USER_WITH_ID_NOT_FOUND.formatted(userHead), "headId", userHead)
                    )
            );
        }
        project.setUserHead(user);
        project.setName(name);
        projectRepository.save(project);
        ProjectDto projectDto = projectMapper.projectEntityToProjectDto(project);
        projectDto.setTasksId(project.getTaskEntityList().stream().map(TaskEntity::getId).toList());
        return projectDto;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ProjectDto findById(Long projectId) {
        ProjectEntity project = entityManager.find(ProjectEntity.class, projectId, ProjectEntityGraph.getProperties(
                projectEntityGraph.getProjectEntityGraphWithTasksAndUserHead()
        ));
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

    @Override
    @Retryable(maxAttempts = 2)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ProjectDto update(ProjectDto projectDto) {
        ProjectEntity project = entityManager.find(ProjectEntity.class, projectDto.getId(), ProjectEntityGraph.getProperties(
                projectEntityGraph.getProjectEntityGraphWithTasksAndUserHead()
        ));
        if (project == null){
            throw  new ProjectNotFoundException(
                    ApiValidationError.getApiValidationError(projectDto,
                            ValidationConstant.PROJECT_WITH_ID_NOT_FOUND.formatted(projectDto.getId()),
                            "projectId", projectDto.getId()));
        }
        if (projectDto.getUserHead() != null) {
            var user = userRepository.findById(projectDto.getUserHead()).orElseThrow(
                    () -> new UserNotFoundException(
                            getApiValidationError(projectDto,
                                    ValidationConstant.USER_WITH_ID_NOT_FOUND.formatted(projectDto.getUserHead()), "headId", projectDto.getUserHead())
                    )
            );
            project.setUserHead(user);
        }
        projectMapper.updateProjectEntityFromProjectDto(projectDto, project);
        ProjectDto updatedProjectDto = projectMapper.projectEntityToProjectDto(project);
        updatedProjectDto.setTasksId(project.getTaskEntityList().stream().map(TaskEntity::getId).toList());
        projectRepository.save(project);
        return updatedProjectDto;
    }
}
