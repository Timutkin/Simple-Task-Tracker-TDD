package ru.timutkin.tdd.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.timutkin.tdd.exception.*;

import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {IncorrectFieldException.class, IncorrectPathVariableException.class})
    protected ResponseEntity<ApiError> handleValidationException(ValidationException exception){
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                List.of(exception.getValidationError()),
                exception.getMessage()
        );
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(value = {TaskNotFoundException.class, UserNotFoundException.class, })
    protected ResponseEntity<ApiError> handleNotFoundException(NotFoundException exception){
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                List.of(exception.getValidationError()),
                exception.getMessage()
        );
        return ResponseEntity.badRequest().body(apiError);
    }



}
