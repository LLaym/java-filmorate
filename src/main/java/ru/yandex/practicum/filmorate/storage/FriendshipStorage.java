package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

public interface FriendshipStorage {
    Friendship save(int id, int friendId);

    boolean delete(int id, int friendId);
}
