package ru.timutkin.tdd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.timutkin.tdd.dto.ProjectDto;
import ru.timutkin.tdd.store.entity.ProjectEntity;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
     @Mapping(target = "userHead", source = "userHead.id")
     ProjectDto projectEntityToProjectDto(ProjectEntity project);
}
