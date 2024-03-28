package ru.yandex.practicum.filmorate.dao;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User add(User user);

    User update(User user);

    void delete(long id);

    List<User> getStorage();

    User getById(long id);
}
