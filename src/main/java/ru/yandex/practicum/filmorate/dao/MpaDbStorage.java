package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MpaRating> mpaRatingRowMapper = (rs, rowNum) ->
            new MpaRating(rs.getInt("mpa_id"), rs.getString("name"));

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MpaRating> findAll() {
        String sql = "SELECT mpa_id, name FROM mpa";
        log.info("Выполнение запроса на получение всех MPA рейтингов.");
        return jdbcTemplate.query(sql, mpaRatingRowMapper);
    }

    @Override
    public Optional<MpaRating> findById(int id) {
        String sqlChecker = "Select mpa_id from mpa where mpa_id = ?";
        List<Integer> mpaId = jdbcTemplate.query(sqlChecker, new Object[]{id}, (rs, rowNum) -> rs.getInt("mpa_id"));
        if (mpaId.isEmpty()) {
            throw new NotExistException(HttpStatus.NOT_FOUND, "Не существует МРА с таким id " + id);
        }
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        log.info("Выполнение запроса на получение MPA рейтинга с ID: {}", id);
        try {
            return jdbcTemplate.query(sql, mpaRatingRowMapper, id).stream().findAny();
        } catch (Exception e) {
            log.error("Ошибка при выполнении запроса на получение MPA рейтинга с ID: {}", id, e);
            throw e;
        }
    }
}
