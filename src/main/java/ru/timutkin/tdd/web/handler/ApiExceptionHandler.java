package ru.timutkin.tdd.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.timutkin.tdd.exception.IncorrectFieldException;
import ru.timutkin.tdd.exception.IncorrectPathVariableException;
import ru.timutkin.tdd.exception.UserNotFoundException;
import ru.timutkin.tdd.exception.ValidationException;

import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {IncorrectFieldException.class, UserNotFoundException.class, IncorrectPathVariableException.class})
    protected ResponseEntity<ApiError> handleValidationException(ValidationException exception){
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                List.of(exception.getValidationError()),
                exception.getMessage()
        );
        return ResponseEntity.badRequest().body(apiError);
    }



}
