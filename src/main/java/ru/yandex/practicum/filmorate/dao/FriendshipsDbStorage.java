package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FriendshipsDbStorage implements FriendshipsStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userStorage;

    @Autowired
    public FriendshipsDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }


    @Override
    public void addFriend(long firstUserId, long secondUserId) {
        if (!userStorage.getById(firstUserId).isEmpty() && !userStorage.getById(secondUserId).isEmpty()) {
            String sql = "INSERT into friendships (user_id, friend_id) values (?, ?)";
            log.info("Добавление дружбы между пользователями {} и {}", firstUserId, secondUserId);
            jdbcTemplate.update(sql, firstUserId, secondUserId);
            jdbcTemplate.update(sql, secondUserId, firstUserId);
        }
    }

    @Override
    public void deleteFriend(long firstUserId, long secondUserId) {
        if (!userStorage.getById(firstUserId).isEmpty() && !userStorage.getById(secondUserId).isEmpty()) {
            String sql = "DELETE from friendships  WHERE user_id = ? AND friend_id = ?";
            log.info("Удаление дружбы между пользователями {} и {}", firstUserId, secondUserId);
            jdbcTemplate.update(sql, firstUserId, secondUserId);
        }
    }

    @Override
    public Set<User> getAllSameFriends(long id, long otherId) {
        String sql = "SELECT f1.friend_id " +
                "FROM friendships f1 " +
                "JOIN friendships f2 ON f1.friend_id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";
        log.info("Поиск общих друзей между пользователями {} и {}", id, otherId);
        List<Long> friendIds = jdbcTemplate.query(sql, new Object[]{id, otherId}, (rs, rowNum) -> rs.getLong("friend_id"));
        Set<User> commonFriends = friendIds.stream()
                .map(i -> userStorage.getById(i).get())
                .collect(Collectors.toSet());
        return commonFriends;
    }

    @Override
    public List<User> getAllFriends(long id) {
        if (!userStorage.getById(id).isEmpty()) {
            String sql = "SELECT * from friendships where user_id = ?";
            log.info("Поиск друзей пользователя {}", id);
            List<Long> friendsId = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("friend_id"), id);
            List<User> friends = friendsId.stream()
                    .map(i -> userStorage.getById(i).get())
                    .collect(Collectors.toList());
            return friends;
        }
        return null;
    }
}
