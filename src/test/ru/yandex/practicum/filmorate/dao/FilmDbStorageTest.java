package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {


    private final JdbcTemplate jdbcTemplate;

    @Test
    void testAddFilm() {
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        Film film = new Film("TestFilm", "description", LocalDate.of(2000, 10, 12), 120, 1L, 1);
        Film savedFilm = filmDbStorage.add(film);
        assertThat(savedFilm).isNotNull();
        assertThat(savedFilm.getId()).isPositive();
    }

    @Test
    void testGetFilmById() {
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        Film film = new Film("TestFilm", "description", LocalDate.of(2000, 10, 12), 120, 1L, 1);
        filmDbStorage.add(film);
        long id = film.getId();
        Film foundFilm = filmDbStorage.getById(id);
        assertThat(foundFilm).isNotNull();
        assertThat(foundFilm.getId()).isEqualTo(id);
    }

    @Test
    void testDeleteFilm() {
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        Film film = new Film("TestFilm", "description", LocalDate.of(2000, 10, 12), 120, 1L, 1);
        filmDbStorage.add(film);
        int sizeBeforeDelete = filmDbStorage.getStorage().size();

        filmDbStorage.delete(film.getId());
        int sizeAfterDelete = filmDbStorage.getStorage().size();

        assertThat(sizeBeforeDelete).isGreaterThan(sizeAfterDelete);
    }

    @Test
    void testUpdateFilm() {
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        Film film = new Film("TestFilm", "description", LocalDate.of(2000, 10, 12), 120, 1L, 1);
        filmDbStorage.add(film);
        Film updatedFilm = filmDbStorage.update(film);
        assertThat(updatedFilm).isNotNull();
    }

//    @Test
//    void testAddLike() {
//        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
//        Film film = new Film("TestFilm", "description", LocalDate.of(2000, 10, 12), 120, 1L, 1);
//        User newUser = new User("newuser@email.com", "newlogin", "New User", LocalDate.of(2000, 1, 1));
//        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
//        userStorage.add(newUser);
//        filmDbStorage.add(film);
//        long filmId = film.getId(), userId = newUser.getId();
//        long initialLikes = film.getRate();
//
//        filmDbStorage.addLike(filmId, userId);
//        long updatedLikes = film.getRate();
//        assertThat(updatedLikes).isEqualTo(initialLikes + 1);
//    }

//    @Test
//    void testDeleteLike() {
//        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
//        Film film = new Film("TestFilm", "description", LocalDate.of(2000, 10, 12), 120, 1L, 1);
//        User newUser = new User("newuser@email.com", "newlogin", "New User", LocalDate.of(2000, 1, 1));
//        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
//        userStorage.add(newUser);
//        filmDbStorage.add(film);
//        long filmId = film.getId(), userId = newUser.getId();
//        long initialLikes = film.getRate();
//        filmDbStorage.deleteLike(filmId, userId);
//        long updatedLikes = film.getRate();
//        assertThat(updatedLikes).isEqualTo(initialLikes - 1);
//    }

    @Test
    void testGetTopFilms() {
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        Film film = new Film("TestFilm", "description", LocalDate.of(2000, 10, 12), 120, 1L, 1);
        Film film2 = new Film("TestFilm", "description", LocalDate.of(2000, 10, 12), 120, 1L, 1);
        Film film3 = new Film("TestFilm", "description", LocalDate.of(2000, 10, 12), 120, 1L, 1);
        filmDbStorage.add(film);
        filmDbStorage.add(film2);
        filmDbStorage.add(film3);
        int count = 5;
        List<Film> topFilms = filmDbStorage.getTopFilms(count);
        assertThat(topFilms).hasSizeLessThanOrEqualTo(count);
    }
}
