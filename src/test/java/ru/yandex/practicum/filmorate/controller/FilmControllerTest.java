package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void validateWrongDate() {
        Film film = Film.builder()
                .name("Test")
                .description("Test des")
                .releaseDate(LocalDate.of(1700, 11, 11))
                .duration(100)
                .build();
        Assertions.assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void validateWrongName() {
        Film film = Film.builder()
                .name(" ")
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.")
                .releaseDate(LocalDate.of(1900, 11, 11))
                .duration(100)
                .build();

        Assertions.assertThrows(MethodArgumentNotValidException.class, () -> filmController.create(film));
    }
}