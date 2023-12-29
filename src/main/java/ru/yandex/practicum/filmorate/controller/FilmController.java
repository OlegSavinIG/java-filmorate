package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends BaseController<Film> {
    @Override
    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.info("Дата указана неправильно");
            throw new ValidationException("Дата релиза до 28.12.1895 не принимается");
        }
//        if (film == null) {
//            log.info("Пустое тело запроса");
//            throw new ValidationException("Не передан фильм для добавления");
//        }
//        if (film.getName() == null) {
//            log.info("Название фильма пустое");
//            throw new ValidationException("Не передано название фильма");
//        }
//        if (film.getDescription().length() > 200) {
//            log.info("Слишком длинное описание");
//            throw new ValidationException("Длина описания больше допустимой, 200 символов");
//        }

//        if (film.getDuration() < 0) {
//            log.info("Неправильно указана продолжительность фильма");
//            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
//        }
    }
    @GetMapping
    public List<Film> getFilms () {
        log.info("Вызов списка фильмов");
        return super.getStorage();
    }
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создание нового фильма");
        return super.create(film);
    }
    @PutMapping
    public Film update (@Valid @RequestBody Film film) {
        log.info("Обновление данных фильма");
        return super.update(film);
    }
}
