package ru.timutkin.tdd.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.timutkin.tdd.enumeration.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskDto {
    Long id;
    @JsonProperty(value = "date_time_of_creation")
    LocalDateTime dataTimeOfCreation;
    String taskName;
    String message;
    Status status;
    Long userId;

}
