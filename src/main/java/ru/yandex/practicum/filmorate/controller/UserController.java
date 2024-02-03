package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@NotNull @PathVariable Long id) {
        log.info("Получение пользователя");
        return userService.getById(id);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Вызов списка пользователей");
        return userService.getStorage();
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@NotNull @PathVariable Long id) {
        log.info("Получение списка друзей");
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getAllSameFriends(@NotNull @PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получения списка общих друзей");
        return userService.getAllSameFriends(id, otherId);
    }


    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создание пользователя");
        user.setFriendList(new HashSet<>());
        return userService.createUser(user);

    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Удаления из списка друзей");
        userService.deleteFriend(id, friendId);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@NotNull @PathVariable Long id) {
        log.info("Удаление пользователя");
        userService.delete(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Добавление друга");
        userService.addFriend(id, friendId);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя");
        return userService.update(user);
    }
}
