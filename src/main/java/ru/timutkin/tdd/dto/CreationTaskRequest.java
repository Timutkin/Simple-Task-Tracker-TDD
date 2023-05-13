package ru.timutkin.tdd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreationTaskRequest {
    @JsonProperty("task_name")
    @NotBlank
    @NotNull
    String taskName;
    @NotBlank
    @NotNull
    String message;
    @NotNull
    Long userId;
    @JsonProperty("project_id")
    @NotNull
    Long projectId;
}
