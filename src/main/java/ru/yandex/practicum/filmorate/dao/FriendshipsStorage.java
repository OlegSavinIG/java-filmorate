package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface FriendshipsStorage {
    void addFriend(long firstUserId, long secondUserId);

    void deleteFriend(long firstUserId, long secondUserId);

    Set<User> getAllSameFriends(long id, long otherId);

    List<User> getAllFriends(long id);
}
