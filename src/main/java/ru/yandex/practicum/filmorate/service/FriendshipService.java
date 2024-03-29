package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendshipsStorage;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

@Service
public class FriendshipService {
    private final FriendshipsStorage friendshipsStorage;

    @Autowired
    public FriendshipService(FriendshipsStorage friendshipsStorage) {
        this.friendshipsStorage = friendshipsStorage;
    }


    public void addFriend(long firstUserId, long secondUserId) {
        friendshipsStorage.addFriend(firstUserId, secondUserId);
    }

    public void deleteFriend(long firstUserId, long secondUserId) {
        friendshipsStorage.deleteFriend(firstUserId, secondUserId);
    }

    public Set<User> getAllSameFriends(long firstUserId, long secondUserId) {
        return friendshipsStorage.getAllSameFriends(firstUserId, secondUserId);
    }

    public List<User> getAllFriends(long id) {
        List<User> allFriends = friendshipsStorage.getAllFriends(id);
        if (allFriends.isEmpty()) {
            throw new NotExistException(HttpStatus.BAD_REQUEST, "У пользователя нет друзей или неверный id");
        }
        return allFriends;
    }
}
