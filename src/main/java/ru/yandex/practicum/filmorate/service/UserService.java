package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        int generatedId = userStorage.save(user);
        User createdUser = userStorage.getById(generatedId).get();

        log.info("Добавлен пользователь: {}", createdUser);
        return createdUser;
    }

    public User updateUser(User user) {
        userStorage.update(user);
        User updatedUser = userStorage.getById(user.getId()).get();

        log.info("Обновлён пользователь: {}", updatedUser);
        return updatedUser;
    }

    public List<User> findAllUsers() {
        log.info("Возвращен список всех пользователей");
        return userStorage.getAll();
    }

    public User findUserById(Integer userId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));

        log.info("Получен пользователь: {}", user);
        return user;
    }

    public void makeFriendship(Integer userId, Integer friendId) {
        log.info("Пользователь с id {} и пользователь с id {} теперь друзья!", userId, friendId);
        friendshipStorage.save(userId, friendId);
    }

    public boolean dropFriendship(Integer id, Integer friendId) {
        log.info("Пользователь с id {} и пользователь с id {} больше не друзья.", id, friendId);
        return friendshipStorage.delete(id, friendId);
    }

    public List<User> findUserFriends(Integer userId) {
        List<User> userFriends = friendshipStorage.getAllByUserId(userId)
                .stream()
                .map(Friendship::getFriendId)
                .map(userStorage::getById)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Возвращен список друзей пользователя: {}", userFriends);
        return userFriends;
    }

    public List<User> findUsersMutualFriends(Integer userId, Integer otherId) {
        List<Integer> user1Friends = friendshipStorage.getAllByUserId(userId)
                .stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toList());
        List<Integer> user2Friends = friendshipStorage.getAllByUserId(otherId)
                .stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toList());

        List<Integer> common = new ArrayList<>(user1Friends);
        common.retainAll(user2Friends);

        List<User> mutualFriends = common.stream()
                .map(userStorage::getById)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Возвращен список общих друзей: {}", mutualFriends);
        return mutualFriends;
    }
}