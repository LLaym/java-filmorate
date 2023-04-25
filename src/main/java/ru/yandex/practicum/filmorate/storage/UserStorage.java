package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User saveUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Integer id);

    void saveFriendship(Integer id, Integer friendId);

    void removeFriendship(Integer id, Integer friendId);
}