package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserStorage userStorage;
    UserService userService;
    UserController userController;

    @BeforeEach
    void BeforeEach() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService, userStorage);
    }

    @Test
    void shouldAdd() {
        User user = new User();
        user.setName("test_name");
        user.setEmail("test@yandex.ru");
        user.setLogin("test_login");
        user.setBirthday(LocalDate.of(1995, Month.MAY, 11));
        userController.createUser(user);
        assertEquals(1, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipEmptyEmail() {
        User user = new User();
        user.setName("test_name");
        user.setLogin("test_login");
        user.setBirthday(LocalDate.of(1995, Month.MAY, 11));
        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(0, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipUnsupportedEmail() {
        User user = new User();
        user.setName("test_name");
        user.setEmail("yandex.ru");
        user.setLogin("test_login");
        user.setBirthday(LocalDate.of(1995, Month.MAY, 11));
        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(0, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipEmptyLogin() {
        User user = new User();
        user.setName("test_name");
        user.setEmail("test@yandex.ru");
        user.setBirthday(LocalDate.of(1995, Month.MAY, 11));
        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(0, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipUnsupportedLogin() {
        User user = new User();
        user.setName("test_name");
        user.setEmail("test@yandex.ru");
        user.setLogin("Vasya Login");
        user.setBirthday(LocalDate.of(1995, Month.MAY, 11));
        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(0, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipUnsupportedBirthday() {
        User user = new User();
        user.setName("test_name");
        user.setEmail("test@yandex.ru");
        user.setLogin("test_login");
        user.setBirthday(LocalDate.of(2999, Month.MAY, 11));
        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(0, userController.findAllUsers().size());
    }
}