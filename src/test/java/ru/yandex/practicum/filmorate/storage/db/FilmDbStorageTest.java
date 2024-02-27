package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(FilmDbStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private Film testFilm;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE From films;");
        testFilm = Film.builder()
                .id(Long.valueOf(1))
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(120)
                .build();
        testFilm = filmDbStorage.add(testFilm);
    }

    @Test
    void findByIdTest() {
        Film foundFilm = filmDbStorage.getById(testFilm.getId());
        assertThat(foundFilm).isNotNull().usingRecursiveComparison().isEqualTo(testFilm);
    }

    @Test
    void updateFilmTest() {
        testFilm.setName("Updated Name");
        filmDbStorage.update(testFilm);
        Film updatedFilm = filmDbStorage.getById(testFilm.getId());
        assertThat(updatedFilm.getName()).isEqualTo("Updated Name");
    }

    @Test
    void deleteFilmTest() {
        filmDbStorage.delete(testFilm.getId());
        List<Film> storage = filmDbStorage.getStorage();
        assertThat(storage).doesNotContain(testFilm);
    }

    @Test
    void findAllFilmsTest() {
        List<Film> films = filmDbStorage.getStorage();
        assertThat(films).isNotEmpty().contains(testFilm);
    }
}
