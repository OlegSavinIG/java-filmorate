package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(UserDbStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;
    private User testUser;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM users");
        testUser = User.builder()
                .id(Long.valueOf(1))
                .email("user@example.com")
                .login("userLogin")
                .name("UserName")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        testUser = userDbStorage.add(testUser);
    }

    @Test
    void saveUserTest() {
        User savedUser = userDbStorage.add(testUser);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    void findByIdTest() {
        User savedUser = userDbStorage.add(testUser);
        User foundUser = userDbStorage.getById(savedUser.getId());
        assertThat(foundUser).isNotNull().usingRecursiveComparison().isEqualTo(savedUser);
    }

    @Test
    void updateUserTest() {
        User savedUser = userDbStorage.add(testUser);
        savedUser.setEmail("updated@example.com");
        userDbStorage.update(savedUser);
        User updatedUser = userDbStorage.getById(savedUser.getId());
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void deleteUserTest() {
        userDbStorage.delete(testUser.getId());
        List<User> storage = userDbStorage.getStorage();
        assertThat(storage).doesNotContain(testUser);
    }

    @Test
    void findAllUsersTest() {
        List<User> films = userDbStorage.getStorage();
        assertThat(films).isNotEmpty().contains(testUser);
    }
}

