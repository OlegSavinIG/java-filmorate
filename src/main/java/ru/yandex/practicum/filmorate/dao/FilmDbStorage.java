package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmRowMapper;


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmMapper filmRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmRowMapper = filmRowMapper;
    }


    @Override
    public Film add(Film film) {
        if (filmValidator(film)) {
            throw new NotExistException(HttpStatus.BAD_REQUEST, "Проблема с передачей данных фильма");
        }
        String sql = "INSERT INTO films (name, description, release_date, duration, rate, mpa_id) VALUES (?, ?, ?, ?, ?, ?)";
        log.info("Добавление фильма {}", film);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getRate());
            ps.setInt(6, film.getMpa().getId());
            return ps;
        }, keyHolder);
        long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);
        addFilmGenres(filmId, film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList()));
        log.info("Фильм успешно добавлен с ID: {}", filmId);
        return film;
    }

    @Override
    public List<Film> getStorage() {
        String sql = "SELECT * FROM films";
        log.info("Получение списка всех фильмов");
        return jdbcTemplate.query(sql, filmRowMapper);
    }

    @Override
    public Film getById(long id) {
        String sqlChecker = "Select film_id from films where film_id = ?";
        List<Long> filmIdId = jdbcTemplate.query(sqlChecker, new Object[]{id}, (rs, rowNum) -> rs.getLong("film_id"));
        if (filmIdId.isEmpty()) {
            throw new NotExistException(HttpStatus.NOT_FOUND, "Не существует фильма с таким id " + id);
        }
        String sql = "SELECT * FROM films WHERE film_id = ?";
        log.info("Получение фильма по ID: {}", id);
        return jdbcTemplate.queryForObject(sql, filmRowMapper, id);
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM films WHERE film_id = ?";
        log.info("Удаление фильма с ID: {}", id);
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?," +
                " duration = ?, rate = ?, mpa_id = ? WHERE film_id = ?";
        log.info("Обновление фильма: {}", film);
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());
        log.info("Фильм с ID: {} успешно обновлен", film.getId());
        return film;
    }

    public void addLike(long filmId, long userId) {
        String sql = "UPDATE films SET rate = rate + 1 WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
        String sqlLikesTable = "INSERT into likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlLikesTable, filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        String sql = "UPDATE films SET rate = rate - 1 WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
        String sqlLikesTable = "DELETE from likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlLikesTable, filmId, userId);
    }

    public List<Film> getTopFilms(int count) {
        String sql = "SELECT * from films group by film_id order by rate desc limit ?";
        List<Film> topFilms = jdbcTemplate.query(sql, filmRowMapper, count);
        return topFilms;
    }

    private void addFilmGenres(long filmId, List<Integer> genreIds) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        for (Integer genreId : genreIds) {
            jdbcTemplate.update(sql, filmId, genreId);
        }
    }

    private boolean filmValidator(Film film) {
        String sqlChecker = "Select mpa_id from mpa where mpa_id = ?";
        List<Integer> mpaId = jdbcTemplate.query(sqlChecker, new Object[]{film.getMpa().getId()}, (rs, rowNum) -> rs.getInt("mpa_id"));
        if (mpaId.isEmpty()) {
            return true;
        }
        String sqlGenreChecker = "Select genre_id from genres where genre_id = ?";
        List<Integer> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        for (Integer genreId : genreIds) {
            List<Integer> result = jdbcTemplate.query(sqlGenreChecker, new Object[]{genreId}, (rs, rowNum) -> rs.getInt("genre_id"));
            if (result.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}