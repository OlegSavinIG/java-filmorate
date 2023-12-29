package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.BaseUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseController<T extends BaseUnit> {
    private Map<Long, T> storage = new HashMap<>();
    private long generatedId;

    public T create(T data) {
        validate(data);
        data.setId(++generatedId);
        storage.put(data.getId(), data);
        return data;
    }

    public T update(T data) {
        validate(data);
        if (!storage.containsKey(data.getId())) {
            throw new ValidationException("Этих данных не существует");
        }
        storage.put(data.getId(), data);
        return data;
    }

    public List<T> getStorage() {
        return new ArrayList<>(storage.values());
    }

    public abstract void validate(T t);
}
