package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userRowMapper = new UserMapper();

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        log.info("Добавление пользователя: {}", user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        log.debug("Пользователь добавлен с ID: {}", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        log.info("Обновление пользователя с ID: {}", user.getId());
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()), user.getId());
        log.debug("Пользователь обновлен: {}", user);
        return user;
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        log.info("Удаление пользователя с ID: {}", id);
        jdbcTemplate.update(sql, id);
        log.debug("Пользователь удален с ID: {}", id);
    }

    @Override
    public List<User> getStorage() {
        String sql = "SELECT * FROM users";
        log.info("Получение списка всех пользователей");
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public Optional<User> getById(long id) {
        String sqlChecker = "Select user_id from users where user_id = ?";
        List<Long> userId = jdbcTemplate.query(sqlChecker, new Object[]{id}, (rs, rowNum) -> rs.getLong("user_id"));
        if (userId.isEmpty()) {
            throw new NotExistException(HttpStatus.NOT_FOUND, "Не существует Ппльзователя с таким id " + id);
        }
        String sql = "SELECT * FROM users WHERE user_id = ?";
        log.info("Получение пользователя с ID: {}", id);
        User user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
        return Optional.ofNullable(user);
    }
}
