package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
UserController userController;
    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void validateWrongLogin() {
        User user = User.builder()
                .name("Test")
                .login("test test")
                .email("test@gmail.ru")
                .birthday(LocalDate.of(1985, 11, 11))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userController.validate(user));
    }
    @Test
    void validateWrongName() {
        User user = User.builder()
                .login("test")
                .email("test@gmail.ru")
                .birthday(LocalDate.of(1985, 11, 11))
                .build();
        userController.validate(user);
        Assertions.assertEquals(user.getName(), user.getLogin());
    }
}