package ru.timutkin.tdd.web.request;

import lombok.Builder;
import lombok.Data;
import ru.timutkin.tdd.enumeration.Status;

@Data
@Builder
public class CreationTaskRequest {
    String taskName;
    String message;
    Long userId;
}
