package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    private final UserController userController;

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
        assertEquals(1, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipUnsupportedEmail() {
        User user = User.builder().name("Vitaly").email("yandex.ru")
                .login("LLaym").birthday(LocalDate.of(1995, Month.MAY, 11)).build();

        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(1, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipEmptyLogin() {
        User user = User.builder().name("Vitaly").email("mail@yandex.ru")
                .birthday(LocalDate.of(1995, Month.MAY, 11)).build();

        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(1, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipUnsupportedLogin() {
        User user = User.builder().name("Vitaly").email("mail@yandex.ru")
                .login("LLaym login").birthday(LocalDate.of(1995, Month.MAY, 11)).build();

        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(1, userController.findAllUsers().size());
    }

    @Test
    void shouldSkipUnsupportedBirthday() {
        User user = User.builder().name("Vitaly").email("mail@yandex.ru")
                .login("LLaym").birthday(LocalDate.of(2999, Month.MAY, 11)).build();

        assertThrows(RuntimeException.class, () -> userController.createUser(user));
        assertEquals(1, userController.findAllUsers().size());
    }
}