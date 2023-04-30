package ru.timutkin.tdd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.timutkin.tdd.dto.DepartmentDto;
import ru.timutkin.tdd.store.entity.DepartmentEntity;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {


    default DepartmentEntity fromParam(String name) {
        return DepartmentEntity.builder()
                .name(name)
                .build();
    }
    @Mapping(target = "departmentHead", source = "departmentHead.id")
    DepartmentDto departmentEntityToDepartmentDto(DepartmentEntity departmentEntity);
}
