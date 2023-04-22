package ru.timutkin.tdd.web.validation;

import lombok.experimental.UtilityClass;
import ru.timutkin.tdd.utils.DateFormatHM;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;

@UtilityClass
public class DataValidation {
    public static boolean validate(LocalDateTime date){
        try {
            Instant.from(DateFormatHM.formatter.parse(date.toString()));
            return true;
        } catch (DateTimeException ignored) {
        }
        return false;
    }
}
