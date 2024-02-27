package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MpaRating> mpaRatingRowMapper = (rs, rowNum) ->
            MpaRating.valueOf(rs.getString("name").toUpperCase());

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MpaRating> findAll() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, mpaRatingRowMapper);
    }

    public Optional<MpaRating> findById(int id) {
        String sql = "SELECT * FROM mpa WHERE id = ?";
        return jdbcTemplate.query(sql, mpaRatingRowMapper, id).stream().findAny();
    }
}
