package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {
    Friendship save(int id, int friendId);

    boolean delete(int id, int friendId);

    List<Friendship> getAllByUserId(int userId);
}
