package ru.timutkin.tdd.service;

import ru.timutkin.tdd.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    ProjectDto create(String name, Long userHead, List<Long> tasksId);

    ProjectDto findById(Long projectId);

    void deleteById(Long projectId);
}
