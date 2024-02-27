package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.annotation.MinFilmDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;


@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Film extends BaseUnit {
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    @MinFilmDate
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    private Set<Long> likes;
    private Set<Genre> genres;
    private MpaRating mpaRating;

    public int getLikesSize() {
        return likes.size();
    }

}
