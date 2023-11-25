package ru.practicum.util.exeptions;

public class ValidationDateException extends RuntimeException {

    public ValidationDateException(ExceptionMessages message) {
        super(message.getDescription());
    }
}