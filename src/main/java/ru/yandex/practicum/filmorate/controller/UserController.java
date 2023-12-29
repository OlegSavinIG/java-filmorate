package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends BaseController<User> {
    @Override
    public void validate(User user) {
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы или быть пустым");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

    }

    @GetMapping
    public List<User> getAll() {
        log.info("Вызов списка пользователей");
        return super.getStorage();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создание пользователя");
        return super.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновление пользователя");
        return super.update(user);
    }
}
