package ru.timutkin.tdd.web.handler;

import lombok.Builder;
import lombok.Data;
import ru.timutkin.tdd.web.validation.CreateUpdateTask;

@Data
@Builder
public class ApiValidationError implements ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public static ApiValidationError getApiValidationError(CreateUpdateTask task, String message, String field, Object value){
        return ApiValidationError.builder()
                .message(message)
                .object(task.getClass().getSimpleName())
                .rejectedValue(value)
                .field(field)
                .build();
    }
}
