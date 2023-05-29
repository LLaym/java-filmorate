package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    int save(User user);

    boolean update(User user);

    Optional<User> findById(int userId);

    List<User> findAll();

    boolean deleteById(int userId);

    boolean existsById(Integer id);
}