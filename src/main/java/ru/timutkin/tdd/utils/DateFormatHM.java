package ru.timutkin.tdd.utils;



import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateFormatHM {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
    public static final LocalDateTime allTime = LocalDateTime.parse("2023-03-03T00:00", DateFormatHM.formatter);

    public static LocalDateTime getDateTime() {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        return LocalDateTime.parse(formattedDateTime, formatter);
    }


}
