package ru.timutkin.tdd.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.timutkin.tdd.exception.already_exists.AlreadyExistsException;
import ru.timutkin.tdd.exception.already_exists.TaskAlreadyExistsException;
import ru.timutkin.tdd.exception.not_found.*;
import ru.timutkin.tdd.exception.validation.IncorrectFieldException;
import ru.timutkin.tdd.exception.validation.IncorrectPathVariableException;
import ru.timutkin.tdd.exception.validation.IncorrectRequestParamException;
import ru.timutkin.tdd.exception.validation.ValidationException;
import ru.timutkin.tdd.web.handler.error_objects.ApiError;

import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {IncorrectFieldException.class, IncorrectPathVariableException.class, IncorrectRequestParamException.class})
    protected ResponseEntity<ApiError> handleValidationException(ValidationException exception){
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                List.of(exception.getValidationError()),
                exception.getMessage()
        );
        return ResponseEntity.badRequest().body(apiError);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {TaskNotFoundException.class, UserNotFoundException.class, ProjectNotFoundException.class, DepartmentNotFoundException.class})
    protected ResponseEntity<ApiError> handleNotFoundException(NotFoundException exception){
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                List.of(exception.getValidationError()),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {TaskAlreadyExistsException.class})
    protected ResponseEntity<ApiError> handleAlreadyExistsException(AlreadyExistsException exception){
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT,
                List.of(exception.getValidationError()),
                exception.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

}
