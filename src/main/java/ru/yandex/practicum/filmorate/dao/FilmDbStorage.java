package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@Slf4j
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Film> filmRowMapper = (ResultSet rs, int rowNum) -> Film.builder()
            .id(rs.getLong("film_id"))
            .name(rs.getString("name"))
            .description(rs.getString("description"))
            .releaseDate(rs.getDate("release_date").toLocalDate())
            .duration(rs.getInt("duration"))
            .rate(rs.getInt("rate"))
            .build();

    @Override
    public Film add(Film film) {
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
            ps.setInt(6, film.getMpaRatingId());
            return ps;
        }, keyHolder);
        long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);
        addFilmGenres(filmId, film.getGenresIds());
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
                film.getDuration(), film.getRate(), film.getMpaRatingId(), film.getId());
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

    public void addFilmGenres(long filmId, List<Integer> genreIds) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        for (Integer genreId : genreIds) {
            jdbcTemplate.update(sql, filmId, genreId);
        }
    }
}