package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @Test
    void testSave() {
        User expectedUser = User.builder()
                .name("Vitaly")
                .email("mail@yandex.ru")
                .login("LLaym")
                .birthday(LocalDate.of(1990, Month.OCTOBER, 25))
                .build();

        userStorage.save(expectedUser);

        User user = userStorage.findById(1).get();

        assertEquals(user.getId(), 1);
        assertEquals(user.getName(), expectedUser.getName());
        assertEquals(user.getEmail(), expectedUser.getEmail());
        assertEquals(user.getLogin(), expectedUser.getLogin());
        assertEquals(user.getBirthday(), expectedUser.getBirthday());
    }

    @Test
    void testUpdate() {
        User oldUser = User.builder()
                .name("Vitaly")
                .email("mail@yandex.ru")
                .login("LLaym")
                .birthday(LocalDate.of(1990, Month.OCTOBER, 25))
                .build();

        userStorage.save(oldUser);

        User expectedUser = User.builder()
                .id(1)
                .name("Updated Vitaly")
                .email("mail@yandex.ru")
                .login("LLaym")
                .birthday(LocalDate.of(1990, Month.OCTOBER, 25))
                .build();

        userStorage.update(expectedUser);

        User updatedUser = userStorage.findById(1).get();

        assertEquals(updatedUser.getId(), expectedUser.getId());
        assertEquals(updatedUser.getName(), expectedUser.getName());
        assertEquals(updatedUser.getEmail(), expectedUser.getEmail());
        assertEquals(updatedUser.getLogin(), expectedUser.getLogin());
        assertEquals(updatedUser.getBirthday(), expectedUser.getBirthday());
    }

    @Test
    void testGetById() {
        User expectedUser = User.builder()
                .name("Vitaly")
                .email("mail@yandex.ru")
                .login("LLaym")
                .birthday(LocalDate.of(1990, Month.OCTOBER, 25))
                .build();

        userStorage.save(expectedUser);

        User user = userStorage.findById(1).get();

        assertEquals(user.getId(), 1);
        assertEquals(user.getName(), expectedUser.getName());
        assertEquals(user.getEmail(), expectedUser.getEmail());
        assertEquals(user.getLogin(), expectedUser.getLogin());
        assertEquals(user.getBirthday(), expectedUser.getBirthday());
    }

    @Test
    void testGetAll() {
        User expectedUser = User.builder()
                .name("Vitaly")
                .email("mail@yandex.ru")
                .login("LLaym")
                .birthday(LocalDate.of(1990, Month.OCTOBER, 25))
                .build();

        userStorage.save(expectedUser);

        List<User> users = userStorage.findAll();

        assertEquals(1, users.size());
        assertEquals(users.get(0).getId(), 1);
        assertEquals(users.get(0).getName(), expectedUser.getName());
        assertEquals(users.get(0).getEmail(), expectedUser.getEmail());
        assertEquals(users.get(0).getLogin(), expectedUser.getLogin());
        assertEquals(users.get(0).getBirthday(), expectedUser.getBirthday());
    }
}