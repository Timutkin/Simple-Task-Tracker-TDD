package ru.timutkin.tdd.dto;


import lombok.Builder;
import lombok.Data;
import ru.timutkin.tdd.web.validation.CreateUpdateTask;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskDto implements CreateUpdateTask {
    Long id;
    LocalDateTime dataTimeOfCreation;
    String taskName;
    String message;
    String status;
    Long userId;
}
