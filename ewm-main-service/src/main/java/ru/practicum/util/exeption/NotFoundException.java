package ru.practicum.util.exeption;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}