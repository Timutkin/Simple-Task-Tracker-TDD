package ru.timutkin.tdd.mapper;

import org.mapstruct.*;

import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.store.entity.TaskEntity;



@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "projectId", source = "project.id")
    TaskDto taskEntityToTaskDto(TaskEntity task);

    TaskEntity taskDtoToTaskEntity(TaskDto task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskEntityFromTaskDto(TaskDto taskDto, @MappingTarget TaskEntity taskEntity);
}
