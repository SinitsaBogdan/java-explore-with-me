package ru.practicum.util.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        return ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("Запрашиваемый объект не найден!")
                .message(e.getMessage())
                .errorTimestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(RequestException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Не корректный запрос.")
                .message(e.getMessage())
                .errorTimestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(CustomException e) {
        return ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .reason("Не соблюдены условия для выполнения операции!")
                .message(e.getMessage())
                .errorTimestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBindException(BindException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message("Field: " + e.getFieldError().getField() +
                        ". Error: " + e.getFieldError().getDefaultMessage() +
                        ". Value: " + e.getFieldError().getRejectedValue())
                .errorTimestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBindException(MissingServletRequestParameterException e) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Не корректно составленый запрос!")
                .message(e.getMessage())
                .errorTimestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ApiError.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Ограничение целостности было нарушено!")
                .message(e.getMessage())
                .errorTimestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUnhandled(Exception e) {
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("Внутренняя ошибка сервера!")
                .message(e.getClass() + " - " + e.getMessage())
                .errors(e.getStackTrace())
                .errorTimestamp(LocalDateTime.now())
                .build();
    }
}