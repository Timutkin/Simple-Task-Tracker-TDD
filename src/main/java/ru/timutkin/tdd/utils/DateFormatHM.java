package ru.timutkin.tdd.utils;



import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateFormatHM {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static LocalDateTime getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        return LocalDateTime.parse(formattedDateTime, formatter);
    }

}
