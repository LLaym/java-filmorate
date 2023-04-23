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

    public Collection<User> makeFriendship(Integer id, Integer friendId) {
        List<User> friends = (List<User>) userStorage.saveFriendship(id, friendId);

        log.info("Пользователи {} теперь друзья!", friends);

        return friends;
    }

    public Collection<User> dropFriendship(Integer id, Integer friendId) {
        List<User> notFriends = (List<User>) userStorage.removeFriendship(id, friendId);

        log.info("Пользователи {} больше не друзья.", notFriends);

        return notFriends;
    }

    public Collection<User> findUserFriends(Integer id) {
        User user = userStorage.getUserById(id);
        List<User> userFriends = new ArrayList<>();
        user.getFriends().forEach(identifier -> userFriends.add(userStorage.getUserById(identifier)));

        log.info("Возвращен список друзей пользователя: {}", userFriends);

        return userFriends;
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