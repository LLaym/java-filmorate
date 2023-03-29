package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private UserStorage userStorage;

    @Autowired
    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Выполнен POST /users. Пользователь: {}, " +
                "количество пользователей в базе: {}", user, userStorage.findAllUsers().size());
        validate(user);
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Выполнен PUT /users. Пользователь: {}, " +
                "количество пользователей в базе: {}", user, userStorage.findAllUsers().size());
        validate(user);
        return userStorage.updateUser(user);
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Выполнен GET /users");
        return userStorage.findAllUsers();
    }

    private void validate(User user) throws ValidationException {
        RuntimeException e = null;
        boolean isNewUser = user.getId() == 0;
        if (isNewUser) {
            if (user.getName() == null || user.getName().equals("")) {
                user.setName(user.getLogin());
            }
            if (user.getEmail() == null || user.getEmail().equals("") || !user.getEmail().contains("@")) {
                e = new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
            } else if (user.getLogin() == null || user.getLogin().equals("") || user.getLogin().contains(" ")) {
                e = new ValidationException("Логин не может быть пустым и содержать пробелы");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                e = new ValidationException("Дата рождения не может быть в будущем.");
            }
        } else {
            if (userStorage.findAllUsers().stream().noneMatch(user1 -> user1.getId() == user.getId())) {
                e = new ValidationException("Пользователя с таким id не существует");
            }
        }
        if (e != null) {
            log.warn(e.getMessage(), e);
            throw e;
        }
    }
}
