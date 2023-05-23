package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    int save(User user);

    boolean update(User user);

    Optional<User> getById(int userId);

    List<User> getAll();

    Optional<User> deleteById(int userId);
}