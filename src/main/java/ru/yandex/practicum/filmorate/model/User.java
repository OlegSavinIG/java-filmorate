package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseUnit {
    @Email(message = "Некорректно введена почта")
    private String email;
    private String login;
    private String name;
    @Past(message = "День рождения не может быть в будущем")
    private LocalDate birthday;


}
