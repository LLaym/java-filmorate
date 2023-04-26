package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface LikeStorage {
    boolean save(int filmId, int userId);

    boolean delete(int filmId, int userId);

    List<Integer> getPopularFilmsIds(int count);
}

