package ru.timutkin.tdd.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.timutkin.tdd.store.entity.UserEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentDto {
    Long id;

    String name;

    Long departmentHead;
}
