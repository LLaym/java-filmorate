package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validate(user);
        log.info("Добавлен пользователь: {}", user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        log.info("Обновлён пользователь: {}", user);
        return userStorage.updateUser(user);
    }

    public Collection<User> findAllUsers() {
        log.info("Возвращен список всех пользователей");
        return userStorage.getAllUsers();
    }

//    // TODO Метод может быть неверным
//    public String addToFriends(User user1, User user2) {
//        user1.getFriends().add(user2.getId());
//        user2.getFriends().add(user1.getId());
//        return String.format("%s и %s теперь друзья!", user1, user2);
//    }
//    // TODO Метод может быть неверным
//
//    public Collection<User> getMutualFriends(User user1, User user2) {
//        Set<Integer> user1Friends = user1.getFriends();
//        Set<Integer> user2Friends = user2.getFriends();
//        Set<Integer> common = new HashSet<>(user1Friends);
//        common.retainAll(user2Friends);
//
//        return userStorage.getAllUsers().stream()
//                .filter(user -> common.contains(user.getId())).collect(Collectors.toList());
//    }

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
            if (userStorage.getAllUsers().stream().noneMatch(user1 -> user1.getId() == user.getId())) {
                e = new ValidationException("Пользователя с таким id не существует");
            }
        }
        if (e != null) {
            log.warn(e.getMessage(), e);
            throw e;
        }
    }
}
