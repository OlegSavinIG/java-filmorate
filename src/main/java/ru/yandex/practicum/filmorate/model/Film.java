package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class Film extends BaseUnit {
    @NotBlank(message = "Название не может быть пустым")
    private  String name;
    @Size(max = 200)
    private  String description;
    @NotNull
    private  LocalDate releaseDate;
    @Min(1)
    private  int duration;

}
