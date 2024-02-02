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

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    private boolean filmExist(long filmId) {
        return filmStorage.getById(filmId) != null;
    }

    public Film createFilm(Film film) {
        if (getStorage().stream().map(i -> i.getName()).anyMatch(i -> i.equals(film.getName()))) {
            throw new AlreadyExistException("Такой фильм уже существует");
        }
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        if (!filmExist(film.getId())) {
            throw new NotExistException(HttpStatus.NOT_FOUND, "Такого фильмa не существует");
        }
        return filmStorage.update(film);
    }

    public void delete(long id) {
        filmStorage.delete(id);
    }

    public List<Film> getStorage() {
        return filmStorage.getStorage();
    }

    public Film getById(long id) {
        if (!filmExist(id)) {
            throw new NotExistException(HttpStatus.NOT_FOUND, "Такой фильм не существует");
        }
        return filmStorage.getById(id);
    }

    public void addLike(long filmId, long userId) {
        if (filmExist(filmId)) {
            getById(filmId).getLikes().add(userId);
        } else throw new NotExistException(HttpStatus.CONFLICT, "Такого фильм не существует");
    }

    public void deleteLike(long filmId, long userId) {
        if (filmExist(filmId)) {
            Film film = getById(filmId);
            if (!film.getLikes().contains(userId)) {
                throw new NotExistException(HttpStatus.CONFLICT, "Такого пользователя нет среди лайков");
            }
            film.getLikes().remove(userId);
        } else throw new NotExistException(HttpStatus.CONFLICT, "Такого фильм не существует");

    }

    public List<Film> getTopFilms(int count) {
        return getStorage().stream()
                .sorted(Comparator.comparing(Film::getLikesSize).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
