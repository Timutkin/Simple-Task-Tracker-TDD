package ru.timutkin.tdd.service;

import ru.timutkin.tdd.dto.ProjectDto;


public interface ProjectService {
    ProjectDto create(String name, Long userHead);

    ProjectDto findById(Long projectId);

    void deleteById(Long projectId);

    ProjectDto update(ProjectDto projectDto);
}
