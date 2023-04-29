package ru.timutkin.tdd.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreationTaskRequest {
    String taskName;
    String message;
    Long userId;
    Long projectId;
}
