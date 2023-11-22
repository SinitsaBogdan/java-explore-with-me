package ru.practicum.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constants {

    public static final String DATE_TEMPLATE = "yyyy-MM-dd HH:mm:ss";

    private Constants() {
    }

    public static DateTimeFormatter getDefaultDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(DATE_TEMPLATE);
    }

    public static LocalDateTime getMaxDateTime() {
        return LocalDateTime.parse("9999-12-31T23:59:59");
    }

    public static LocalDateTime getMinDateTime() {
        return LocalDateTime.parse("2000-01-01T00:00:00");
    }
}
