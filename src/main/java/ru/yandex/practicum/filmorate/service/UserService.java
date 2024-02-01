package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    public UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private boolean userExist(long userId) {
        User user = userStorage.getById(userId);
        return user != null;
    }

    public User createUser(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (getStorage().stream()
                .map(i -> i.getEmail())
                .anyMatch(i -> i.equals(user.getEmail()))) {
            throw new AlreadyExistException("Такой пользователь уже существует");
        }
        return userStorage.add(user);
    }

    public User update(User user) {
        if (!userExist(user.getId())) {
            throw new NotExistException(HttpStatus.NOT_FOUND, "Такого пользователя не существует");
        }
        return userStorage.update(user);
    }

    public void delete(long id) {
        if (userExist(id)) {
            userStorage.delete(id);
        } else throw new NotExistException(HttpStatus.NOT_FOUND, "Такой пользователь не существует");
    }

    public List<User> getStorage() {
        return userStorage.getStorage();
    }

    public User getById(long id) {
        if (!userExist(id)) {
            throw new NotExistException(HttpStatus.NOT_FOUND, "Такой пользователь не существует");
        }
        return userStorage.getById(id);
    }

    public void addFriend(long firstUserId, long secondUserId) {
        if (userExist(firstUserId) && userExist(secondUserId)) {
            User firstUser = userStorage.getById(firstUserId);
            User secondUser = userStorage.getById(secondUserId);

            firstUser.getFriendList().add(secondUser.getId());
            secondUser.getFriendList().add(firstUser.getId());
        } else throw new NotExistException(HttpStatus.NOT_FOUND, "Одного из ползователей не существует");
    }

    public void deleteFriend(long firstUserId, long secondUserId) {
        if (userExist(firstUserId) && userExist(secondUserId)) {
            User firstUser = userStorage.getById(firstUserId);
            User secondUser = userStorage.getById(secondUserId);

            firstUser.getFriendList().remove(secondUser.getId());
            secondUser.getFriendList().remove(firstUser.getId());
        } else throw new NotExistException(HttpStatus.NOT_FOUND, "Одного из ползователей не существует");
    }

    public Set<User> getAllSameFriends(Long id, Long otherId) {
        if (userExist(id) && userExist(otherId)) {
            User userFirst = getById(id);
            User userSecond = getById(otherId);
            if (userFirst.getFriendList() == null || userSecond.getFriendList() == null) {
                return new HashSet<>();
            }
            Set<User> firstFriendList = userFirst.getFriendList().stream()
                    .map(i -> userStorage.getById(i))
                    .collect(Collectors.toSet());
            Set<User> secondFriendList = userSecond.getFriendList().stream()
                    .map(i -> userStorage.getById(i))
                    .collect(Collectors.toSet());

            return firstFriendList.stream()
                    .filter(f -> secondFriendList.contains(f))
                    .collect(Collectors.toSet());
        } else throw new NotExistException(HttpStatus.NOT_FOUND, "Одного из ползователей не существует");
    }

    public Set<User> getAllFriends(long id) {
        User user = getById(id);
        return user.getFriendList().stream()
                .map(i -> userStorage.getById(i))
                .collect(Collectors.toSet());
    }
}
