package ru.timutkin.tdd.service;

import ru.timutkin.tdd.dto.DepartmentDto;

public interface DepartmentService {
    DepartmentDto create(String name, Long headId);
}
