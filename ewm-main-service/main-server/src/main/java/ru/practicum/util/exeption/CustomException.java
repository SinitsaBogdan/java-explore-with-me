package ru.practicum.util.exeption;

public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}