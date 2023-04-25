package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        log.info("Добавлен пользователь: {}", user);
        return userStorage.saveUser(user);
    }

    public User updateUser(User user) {
        log.info("Обновлён пользователь: {}", user);
        return userStorage.updateUser(user);
    }

    public List<User> findAllUsers() {
        log.info("Возвращен список всех пользователей");
        return userStorage.getAllUsers();
    }

    public User findUserById(Integer id) {
        User user = userStorage.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + id + " не найден"));

        log.info("Получен пользователь: {}", user);
        return user;
    }

    public void makeFriendship(Integer id, Integer friendId) {
        log.info("Пользователь с id {} и пользователь с id {} теперь друзья!", id, friendId);
        userStorage.saveFriendship(id, friendId);
    }

    public void dropFriendship(Integer id, Integer friendId) {
        log.info("Пользователь с id {} и пользователь с id {} больше не друзья.", id, friendId);
        userStorage.removeFriendship(id, friendId);
    }

    public List<User> findUserFriends(Integer id) {
        User user = userStorage.getUserById(id).get();
        List<User> userFriends = new ArrayList<>();

        user.getFriends().forEach(identifier -> userFriends.add(userStorage.getUserById(identifier).get()));

        log.info("Возвращен список друзей пользователя: {}", userFriends);
        return userFriends;
    }

    public List<User> findUsersMutualFriends(Integer id, Integer otherId) {
        User user1 = userStorage.getUserById(id).get();
        User user2 = userStorage.getUserById(otherId).get();

        Set<Integer> user1Friends = user1.getFriends();
        Set<Integer> user2Friends = user2.getFriends();
        Set<Integer> common = new HashSet<>(user1Friends);

        common.retainAll(user2Friends);

        List<User> mutualFriends = new ArrayList<>();

        common.forEach(identifier -> mutualFriends.add(userStorage.getUserById(identifier).get()));

        log.info("Возвращен список общих друзей: {}", mutualFriends);
        return mutualFriends;
    }
}