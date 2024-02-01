package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseUnit {
    @Email(message = "Некорректно введена почта")
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @Past(message = "День рождения не может быть в будущем")
    private LocalDate birthday;
    private Set<Long> friendList;


}
