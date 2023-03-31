package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private static int nextId;
    private UserStorage userStorage;

    public User createUser(User user) {
        validateUser(user);
        user.setId(++nextId);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        log.info("Добавлен пользователь: {}", user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        log.info("Обновлён пользователь: {}", user);
        return userStorage.updateUser(user);
    }

    public Collection<User> findAllUsers() {
        log.info("Возвращен список всех пользователей");
        return userStorage.getAllUsers();
    }

    public User findUserById(Integer id) {
        validateUserId(id);
        User user = userStorage.getUserById(id);
        log.info("Получен пользователь: {}", user);
        return user;
    }

    public Collection<User> makeTwoUsersFriends(Integer id, Integer friendId) {
        validateUserId(id);
        validateUserId(friendId);
        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(friendId);
        user1.getFriends().add(friendId);
        user2.getFriends().add(id);
        log.info("Пользователь {} и {} теперь друзья!", user1, user2);
        return List.of(user1, user2);
    }

    public Collection<User> makeTwoUsersStopBeingFriends(Integer id, Integer friendId) {
        validateUserId(id);
        validateUserId(friendId);
        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(friendId);
        user1.getFriends().remove(friendId);
        user2.getFriends().remove(id);
        log.info("Пользователь {} и {} больше не дружат!", user1, user2);
        return List.of(user1, user2);
    }

    private void validateUserId(Integer id) {
        if (id == null || id <= 0) {
            throw new ValidationException("параметр id не может быть меньше 0");
        }
        if (userStorage.getUserById(id) == null) {
            throw new UserNotFoundException("пользователя с таким id не существует");
        }
    }

    private void validateUser(User user) throws ValidationException {
        boolean isNewUser = user.getId() == 0;
        if (isNewUser) {
            if (user.getEmail() == null || user.getEmail().equals("") || !user.getEmail().contains("@")) {
                throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
            } else if (user.getLogin() == null || user.getLogin().equals("") || user.getLogin().contains(" ")) {
                throw new ValidationException("логин не может быть пустым и содержать пробелы");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("дата рождения не может быть в будущем.");
            }
        } else {
            if (userStorage.getAllUsers().stream().noneMatch(user1 -> user1.getId() == user.getId())) {
                throw new UserNotFoundException("пользователя с таким id не существует");
            }
        }
    }
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
//
//    }
}