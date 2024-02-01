package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    private boolean filmExist(long filmId) {
        return getById(filmId) != null;
    }

    public Film createFilm(Film film) {
        if (getStorage().stream()
                .map(i -> i.getName())
                .anyMatch(i -> i.equals(film.getName()))) {
            throw new AlreadyExistException("Такой фильм уже существует");
        }
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        if (filmStorage.getStorage().stream()
                .anyMatch(i -> i.equals(film))) {
            return filmStorage.add(film);
        }
        throw new NotExistException(HttpStatus.CONFLICT, "Такого фильм не существует");
    }

    public void delete(long id) {
        filmStorage.delete(id);
    }

    public List<Film> getStorage() {
        return filmStorage.getStorage();
    }

    public Film getById(long id) {
        return getById(id);
    }

    public void addLike(long filmId, long userId) {
        if (filmExist(filmId)) {
            getById(filmId).getLikes().add(userId);
        } else throw new NotExistException(HttpStatus.CONFLICT, "Такого фильм не существует");
    }

    public void deleteLike(long filmId, long userId) {
        if (filmExist(filmId)) {
            getById(filmId).getLikes().remove(userId);
        } else throw new NotExistException(HttpStatus.CONFLICT, "Такого фильм не существует");

    }

    public List<Film> getTopFilms(int count) {
        List<Film> films = getStorage().stream().collect(Collectors.toList());
        films.sort(Comparator.comparing(Film::getLikesSize).reversed());
        return films.stream().limit(count).collect(Collectors.toList());
    }
}
