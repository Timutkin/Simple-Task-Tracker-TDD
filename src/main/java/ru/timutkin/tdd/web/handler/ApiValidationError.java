package ru.timutkin.tdd.web.handler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiValidationError implements ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
}
