package ru.timutkin.tdd.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("created_at")
    LocalDateTime createdAt;
    @JsonProperty("task_name")
    String taskName;
    String message;
    String status;
    @JsonProperty("user_id")
    Long userId;
    @JsonProperty("project_id")
    Long projectId;
}
