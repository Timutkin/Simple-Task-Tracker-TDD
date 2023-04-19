package ru.timutkin.tdd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ru.timutkin.tdd.dto.TaskDto;
import ru.timutkin.tdd.entity.TaskEntity;



@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "userId", source = "user.id")
    TaskDto taskEntityToTaskDto(TaskEntity task);
}
