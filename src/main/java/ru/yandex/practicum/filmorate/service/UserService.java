package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
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

        log.info("Добавлен пользователь: {}", user);
        return userStorage.save(user);
    }

    public User updateUser(User user) {
        log.info("Обновлён пользователь: {}", user);
        return userStorage.update(user);
    }

    public List<User> findAllUsers() {
        log.info("Возвращен список всех пользователей");
        return userStorage.getAll();
    }

    public User findUserById(Integer id) {
        User user = userStorage.getById(id);

        log.info("Получен пользователь: {}", user);
        return user;
    }

    public Friendship makeFriendship(Integer id, Integer friendId) {
        log.info("Пользователь с id {} и пользователь с id {} теперь друзья!", id, friendId);
        return friendshipStorage.save(id, friendId);
    }

    public boolean dropFriendship(Integer id, Integer friendId) {
        log.info("Пользователь с id {} и пользователь с id {} больше не друзья.", id, friendId);
        return friendshipStorage.delete(id, friendId);
    }

    public List<User> findUserFriends(Integer id) {
        List<User> userFriends = friendshipStorage.getAllByUserId(id)
                .stream()
                .map(Friendship::getFriendId)
                .map(userStorage::getById)
                .collect(Collectors.toList());

        log.info("Возвращен список друзей пользователя: {}", userFriends);
        return userFriends;
    }

    public List<User> findUsersMutualFriends(Integer id, Integer otherId) {

        List<Integer> user1Friends = friendshipStorage.getAllByUserId(id)
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
                .collect(Collectors.toList());

        log.info("Возвращен список общих друзей: {}", mutualFriends);
        return mutualFriends;
    }
}