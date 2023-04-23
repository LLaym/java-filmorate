package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User saveUser(User user);

    User updateUser(User user);

    Collection<User> getAllUsers();

    User getUserById(Integer id);

    Collection<User> saveFriendship(Integer id, Integer friendId);

    Collection<User> removeFriendship(Integer id, Integer friendId);
}