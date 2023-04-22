package ru.timutkin.tdd.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.timutkin.tdd.web.validation.CreateUpdateTask;


@Data
@AllArgsConstructor
@Builder
public class CreationTaskRequest implements CreateUpdateTask {
    String taskName;
    String message;
    Long userId;
}
