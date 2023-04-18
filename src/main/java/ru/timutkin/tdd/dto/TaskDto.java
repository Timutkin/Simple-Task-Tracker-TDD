package ru.timutkin.tdd.dto;


import lombok.Builder;
import lombok.Data;
import ru.timutkin.tdd.enumeration.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskDto {
    Long id;
    LocalDateTime dataTimeOfCreation;
    String taskName;
    String message;
    Status status;
    Long userId;

}
