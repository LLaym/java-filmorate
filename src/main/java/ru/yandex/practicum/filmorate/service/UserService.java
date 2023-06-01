package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.model.EventOperation.ADD;
import static ru.yandex.practicum.filmorate.model.EventOperation.REMOVE;
import static ru.yandex.practicum.filmorate.model.EventType.FRIEND;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;
    private final EventStorage eventStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       FriendshipStorage friendshipStorage,
                       EventStorage eventStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
        this.eventStorage = eventStorage;
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        int generatedId = userStorage.save(user);
        User createdUser = userStorage.findById(generatedId).orElse(null);

        log.info("Добавлен пользователь: {}", createdUser);
        return createdUser;
    }

    public User updateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        Integer userId = user.getId();

        if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        userStorage.update(user);
        User updatedUser = userStorage.findById(userId).orElse(null);

        log.info("Обновлён пользователь: {}", updatedUser);
        return updatedUser;
    }

    public List<User> getAllUsers() {
        List<User> users = userStorage.findAll();

        log.info("Возвращен список всех пользователей");
        return users;
    }

    public User getUserById(Integer userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        log.info("Получен пользователь: {}", user);
        return user;
    }

    public void deleteUserById(Integer userId) {
        if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        userStorage.deleteById(userId);
        log.info("Пользователь с id {} удален: ", userId);
    }

    public void makeFriendship(Integer userId, Integer friendId) {
        if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else if (userStorage.notExists(friendId)) {
            throw new NotFoundException("Пользователь с id " + friendId + " не найден");
        }

        friendshipStorage.save(userId, friendId);
        log.info("Пользователь с id {} и пользователь с id {} теперь друзья!", userId, friendId);

        Event event = Event.builder()
                .userId(userId)
                .entityId(friendId)
                .eventType(FRIEND)
                .operation(ADD)
                .build();
        eventStorage.save(event);
    }

    public void dropFriendship(Integer userId, Integer friendId) {
        if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else if (userStorage.notExists(friendId)) {
            throw new NotFoundException("Пользователь с id " + friendId + " не найден");
        }

        friendshipStorage.delete(userId, friendId);
        log.info("Пользователь с id {} и пользователь с id {} больше не друзья.", userId, friendId);

        Event event = Event.builder()
                .userId(userId)
                .entityId(friendId)
                .eventType(FRIEND)
                .operation(REMOVE)
                .build();
        eventStorage.save(event);
    }

    public List<User> getUserFriends(Integer userId) {
        if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        List<User> userFriends = friendshipStorage.findAllByUserId(userId)
                .stream()
                .map(Friendship::getFriendId)
                .map(userStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Возвращен список друзей пользователя: {}", userFriends);
        return userFriends;
    }

    public List<User> getUsersMutualFriends(Integer userId, Integer otherId) {
        if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else if (userStorage.notExists(otherId)) {
            throw new NotFoundException("Пользователь с id " + otherId + " не найден");
        }

        List<Integer> user1Friends = friendshipStorage.findAllByUserId(userId)
                .stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toList());
        List<Integer> user2Friends = friendshipStorage.findAllByUserId(otherId)
                .stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toList());

        List<Integer> common = new ArrayList<>(user1Friends);
        common.retainAll(user2Friends);

        List<User> mutualFriends = common.stream()
                .map(userStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Возвращен список общих друзей: {}", mutualFriends);
        return mutualFriends;
    }

    public List<Event> getFeed(Integer userId) {
        return userStorage.findById(userId)
                .map(user -> eventStorage.findAllByUserId(userId))
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }
}