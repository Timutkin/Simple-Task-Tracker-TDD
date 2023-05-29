package ru.timutkin.tdd.web.controller.data;

import ru.timutkin.tdd.dto.ProjectDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectDtoData {

    private static final List<ProjectDto> projects = new ArrayList<>();

    static {
        Collections.addAll(
                projects,
                ProjectDto.builder()
                        .id(1L)
                        .name("Amazing project")
                        .userHead(UserEntityData.getFirstUserEntity().getId())
                        .build(),
                ProjectDto.builder()
                        .id(2L)
                        .name("Awesome project")
                        .userHead(UserEntityData.getSecondUserEntity().getId())
                        .build()
        );
    }

    public static ProjectDto getFirstProjectDto(){
        return projects.get(0);
    }

    public static ProjectDto getSecondProjectDto() {
        return projects.get(1);
    }
}
