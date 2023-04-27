package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User save(User user);

    User update(User user);

    User getById(int id);

    List<User> getAll();
//    void saveFriendship(Integer id, Integer friendId);
//    void removeFriendship(Integer id, Integer friendId);
}