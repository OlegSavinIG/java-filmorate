package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testAddUser() {
        User newUser = new User("newuser@email.com", "newlogin", "New User", LocalDate.of(2000, 1, 1));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        User addedUser = userStorage.add(newUser);

        assertThat(addedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newUser);

    }

    @Test
    public void testUpdateUser() {
        User newUser = new User("updateuser@email.com", "updatelogin", "Update User", LocalDate.of(2000, 2, 2));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User addedUser = userStorage.add(newUser);

        addedUser.setName("Updated Name");
        User updatedUser = userStorage.update(addedUser);

        assertThat(updatedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(addedUser);

    }

    @Test
    public void testDeleteUser() {
        User newUser = new User("deleteuser@email.com", "deletelogin", "Delete User", LocalDate.of(2000, 3, 3));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User addedUser = userStorage.add(newUser);

        userStorage.delete(addedUser.getId());

        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> userStorage.getById(addedUser.getId()));
    }

    @Test
    public void testGetStorage() {
        User newUser = new User("userbyid@email.com", "userlogin", "User Name", LocalDate.of(1995, 5, 5));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User addedUser = userStorage.add(newUser);

        List<User> users = userStorage.getStorage();

        assertThat(users).isNotNull().isNotEmpty();
    }

    @Test
    public void testGetById() {
        User newUser = new User("userbyid@email.com", "userlogin", "User Name", LocalDate.of(1995, 5, 5));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User addedUser = userStorage.add(newUser);

        User foundUser = userStorage.getById(addedUser.getId());

        assertThat(foundUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(addedUser);
    }
}

