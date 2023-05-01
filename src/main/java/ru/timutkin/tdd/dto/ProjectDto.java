package ru.timutkin.tdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDto {
    Long id;

    String name;

    LocalDateTime createdAt;

    List<Long> tasksId = new ArrayList<>();

    Long userHead;
}
