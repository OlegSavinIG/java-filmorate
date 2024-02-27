package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        user.setName(Optional.ofNullable(user.getName()).orElse(user.getLogin()));
        if (userStorage.getStorage().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new AlreadyExistException("Такой пользователь уже существует");
        }
        return userStorage.add(user);
    }
    public List<User> getAllUsers() {
        return userStorage.getStorage(); //
    }

    public User update(User user) {
        return Optional.ofNullable(userStorage.getById(user.getId()))
                .map(existingUser -> userStorage.update(user))
                .orElseThrow(() -> new NotExistException(HttpStatus.NOT_FOUND, "Такого пользователя не существует"));
    }

    public void delete(long id) {
        if (userStorage.getById(id) == null) {
            throw new NotExistException(HttpStatus.NOT_FOUND, "Такой пользователь не существует");
        }
        userStorage.delete(id);
    }

    public User getById(long id) {
        return Optional.ofNullable(userStorage.getById(id))
                .orElseThrow(() -> new NotExistException(HttpStatus.NOT_FOUND, "Такой пользователь не существует"));
    }

    public void addFriend(long firstUserId, long secondUserId) {
        User firstUser = getById(firstUserId);
        User secondUser = getById(secondUserId);
        firstUser.getFriendList().add(secondUserId);
        secondUser.getFriendList().add(firstUserId);
    }

    public void deleteFriend(long firstUserId, long secondUserId) {
        User firstUser = getById(firstUserId);
        User secondUser = getById(secondUserId);
        firstUser.getFriendList().remove(secondUserId);
        secondUser.getFriendList().remove(firstUserId);
    }

    public Set<User> getAllSameFriends(Long id, Long otherId) {
        User userFirst = getById(id);
        User userSecond = getById(otherId);
        Set<Long> commonFriendsIds = new HashSet<>(userFirst.getFriendList());
        commonFriendsIds.retainAll(userSecond.getFriendList());

        return commonFriendsIds.stream()
                .map(this::getById)
                .collect(Collectors.toSet());
    }

    public List<User> getAllFriends(long id) {
        User user = getById(id);
        return user.getFriendList().stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }
}
