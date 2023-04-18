package ru.timutkin.tdd.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class CreationTaskRequest {
    String taskName;
    String message;
    Long userId;
}
