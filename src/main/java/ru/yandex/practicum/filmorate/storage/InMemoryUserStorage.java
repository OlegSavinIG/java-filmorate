package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    Map<Long, User> userStorage = new HashMap<>();
    private long generatedId;

    @Override
    public User add(User user) {
        user.setId(++generatedId);
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(long id) {
        userStorage.remove(id);
    }

    @Override
    public List<User> getStorage() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public User getById(long id) {
        return userStorage.get(id);
    }
}