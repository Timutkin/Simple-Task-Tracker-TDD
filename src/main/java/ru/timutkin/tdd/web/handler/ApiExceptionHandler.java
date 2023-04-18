package ru.timutkin.tdd.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.timutkin.tdd.exception.IncorrectDataException;

import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IncorrectDataException.class)
    protected ResponseEntity<ApiError> handleValidationException(IncorrectDataException incorrectDataException){
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                List.of(incorrectDataException.getValidationError()),
                incorrectDataException.getMessage()
        );
        return ResponseEntity.badRequest().body(apiError);
    }

}
