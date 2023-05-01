package ru.timutkin.tdd.service;

import ru.timutkin.tdd.dto.DepartmentDto;

public interface DepartmentService {
    DepartmentDto create(String name, Long headId);

    DepartmentDto findById(Long departmentId);

    void deleteById(Long departmentId);

    DepartmentDto update(DepartmentDto departmentDto);
}
