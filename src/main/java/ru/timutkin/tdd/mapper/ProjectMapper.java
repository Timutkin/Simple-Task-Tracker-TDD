package ru.timutkin.tdd.mapper;

import org.mapstruct.*;
import ru.timutkin.tdd.dto.ProjectDto;
import ru.timutkin.tdd.store.entity.ProjectEntity;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
     @Mapping(target = "userHead", source = "userHead.id")
     ProjectDto projectEntityToProjectDto(ProjectEntity project);

     @Mapping(target = "userHead", ignore = true)
     @Mapping(target = "taskEntityList", ignore = true)
     @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
     void updateProjectEntityFromProjectDto(ProjectDto projectDto, @MappingTarget ProjectEntity projectEntity);
}
