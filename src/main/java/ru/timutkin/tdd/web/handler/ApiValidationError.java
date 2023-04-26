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

    public static ApiValidationError getApiValidationError(Object object, String message, String field, Object value){
        return ApiValidationError.builder()
                .message(message)
                .object(object.getClass().getSimpleName())
                .rejectedValue(value)
                .field(field)
                .build();
    }

}
