package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendship {
    private User user1;
    private User user2;
    private Status status;
}
