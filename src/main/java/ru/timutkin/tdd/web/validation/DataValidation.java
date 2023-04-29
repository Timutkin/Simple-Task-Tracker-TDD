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
            DateFormatHM.formatter.parse(date.toString());
            return true;
        } catch (DateTimeException ignored) {
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println(validate(LocalDateTime.parse("2023-04-28T23:15:15.087846200")));
    }
}
