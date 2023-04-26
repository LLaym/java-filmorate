package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface LikeStorage {
    void save(int filmId, int userId);

    void delete(int filmId, int userId);

    List<Integer> getPopularFilmsIds(int count);
}

