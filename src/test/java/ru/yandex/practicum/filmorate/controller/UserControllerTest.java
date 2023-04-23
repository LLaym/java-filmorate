package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    UserService userService;
    UserController userController;

    @BeforeEach
    void initEach() {
        userService = new UserService(new UserDbStorage(new JdbcTemplate()));
        userController = new UserController(userService);
    }

    @Test
    void shouldAdd() {
        User user = User.builder().name("Vitaly").email("mail@yandex.ru")
                .login("LLaym").birthday(LocalDate.of(1995, Month.MAY, 11)).build();
        userController.createUser(user);

        assertEquals(1, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipEmptyEmail() {
        User user = User.builder().name("Vitaly")
                .login("LLaym").birthday(LocalDate.of(1995, Month.MAY, 11)).build();

        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(0, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipUnsupportedEmail() {
        User user = User.builder().name("Vitaly").email("yandex.ru")
                .login("LLaym").birthday(LocalDate.of(1995, Month.MAY, 11)).build();

        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(0, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipEmptyLogin() {
        User user = User.builder().name("Vitaly").email("mail@yandex.ru")
                .birthday(LocalDate.of(1995, Month.MAY, 11)).build();

        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(0, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipUnsupportedLogin() {
        User user = User.builder().name("Vitaly").email("mail@yandex.ru")
                .login("LLaym login").birthday(LocalDate.of(1995, Month.MAY, 11)).build();

        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(0, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipUnsupportedBirthday() {
        User user = User.builder().name("Vitaly").email("mail@yandex.ru")
                .login("LLaym").birthday(LocalDate.of(2999, Month.MAY, 11)).build();

        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(0, userController.findAllUsers().size());
    }
}