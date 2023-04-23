package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private UserStorage userStorage;

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

    public Collection<User> findAllUsers() {
        log.info("Возвращен список всех пользователей");

        return userStorage.getAllUsers();
    }

    public User findUserById(Integer id) {
        User user = userStorage.getUserById(id);

        log.info("Получен пользователь: {}", user);

        return user;
    }

    public Collection<User> makeTwoUsersFriends(Integer id, Integer friendId) {
        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(friendId);
        user1.getFriends().add(friendId);
        user2.getFriends().add(id);

        log.info("Пользователь {} и {} теперь друзья!", user1, user2);

        return List.of(user1, user2);
    }

    public Collection<User> makeTwoUsersStopBeingFriends(Integer id, Integer friendId) {
        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(friendId);
        user1.getFriends().remove(friendId);
        user2.getFriends().remove(id);

        log.info("Пользователь {} и {} больше не дружат!", user1, user2);

        return List.of(user1, user2);
    }

    public Collection<User> findUserFriends(Integer id) {
        User user = userStorage.getUserById(id);
        List<User> usersFriends = new ArrayList<>();
        user.getFriends().forEach(identifier -> usersFriends.add(userStorage.getUserById(identifier)));

        log.info("Возвращен список друзей пользователя: {}", usersFriends);

        return usersFriends;
    }

    public Collection<User> findUsersMutualFriends(Integer id, Integer otherId) {
        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(otherId);

        Set<Integer> user1Friends = user1.getFriends();
        Set<Integer> user2Friends = user2.getFriends();
        Set<Integer> common = new HashSet<>(user1Friends);

        common.retainAll(user2Friends);
        List<User> mutualFriends = new ArrayList<>();
        common.forEach(identifier -> mutualFriends.add(userStorage.getUserById(identifier)));

        log.info("Возвращен список общих друзей: {}", mutualFriends);

        return mutualFriends;
    }
}