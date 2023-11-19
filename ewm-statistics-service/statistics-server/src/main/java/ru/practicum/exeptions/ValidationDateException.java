package ru.practicum.exeptions;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class ValidationDateException extends RuntimeException {

    public ValidationDateException(ErrorMessages message) {
        super(message.getDescription());
        log.error("{} : {} : {}", LocalDateTime.now(), message.getCategory(), message.getDescription());
    }
}