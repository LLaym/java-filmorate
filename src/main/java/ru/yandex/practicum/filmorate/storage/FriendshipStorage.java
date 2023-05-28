package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {
    void save(int userId, int friendId);

    boolean delete(int userId, int friendId);

    List<Friendship> findAllByUserId(int userId);
}
