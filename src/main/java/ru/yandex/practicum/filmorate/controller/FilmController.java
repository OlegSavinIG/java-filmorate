package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Вызов списка фильмов");
        return filmService.getStorage();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@NotNull @PathVariable Long id) {
        log.info("Получение фильма");

        return filmService.getById(id);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получение списка лучших фильмов");

        return filmService.getTopFilms(count);
    }

    @PostMapping
    public ResponseEntity<Film> create(@NotNull @Valid @RequestBody Film film) {
        log.info("Создание нового фильма");
        film.setLikes(new HashSet<>());
        Film createdFilm = filmService.createFilm(film);
        return ResponseEntity.ok(createdFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@NotNull @PathVariable Long id, @PathVariable Long userId) {
        log.info("Добавление лайка");

        filmService.addLike(id, userId);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновление данных фильма");
        return filmService.update(film);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@NotNull @PathVariable Long id, @PathVariable Long userId) {
        log.info("Удаление лайка ");

        filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@NotNull @PathVariable Long id) {
        log.info("Удаление фильма");

        filmService.delete(id);
    }
}
