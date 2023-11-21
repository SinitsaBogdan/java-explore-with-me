package ru.practicum.util.exeption;

public class RequestException extends RuntimeException {

    public RequestException(String message) {
        super(message);
    }
}