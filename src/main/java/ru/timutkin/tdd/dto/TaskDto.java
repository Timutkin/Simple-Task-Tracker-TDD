package ru.timutkin.tdd.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    Long id;
    LocalDateTime dataTimeOfCreation;
    String taskName;
    String message;
    String status;
    Long userId;
}
