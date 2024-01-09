package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends BaseController<User> {

    @GetMapping
    public List<User> getAll() {
        log.info("Вызов списка пользователей");
        return super.getStorage();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        log.info("Создание пользователя");
        return super.create(user);

    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя");
        return super.update(user);
    }
}
