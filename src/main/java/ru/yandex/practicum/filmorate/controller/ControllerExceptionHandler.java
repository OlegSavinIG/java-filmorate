package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@ControllerAdvice

public class ControllerExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorResponse validateExceptionHandler(ValidationException e) {
        return new ErrorResponse("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler(NotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse notExistExceptionHandler(NotExistException e) {
        return new ErrorResponse("Ошибка: обьект не найден ", e.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ErrorResponse exceptionHandler(Exception e) {
        return new ErrorResponse("Ошибка", e.getMessage());
    }


}
