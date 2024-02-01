package ru.yandex.practicum.filmorate.exception;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException() {
    }

    public AlreadyExistException(String message) {
        super(message);
    }

    public AlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistException(Throwable cause) {
        super(cause);
    }
}
