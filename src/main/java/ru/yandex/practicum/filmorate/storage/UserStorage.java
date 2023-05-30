package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    int save(User user);

    void update(User user);

    Optional<User> findById(int userId);

    List<User> findAll();

    void deleteById(int userId);

    boolean notExistsById(Integer id);
}