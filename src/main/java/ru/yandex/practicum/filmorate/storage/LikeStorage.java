package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface LikeStorage {
    void saveLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    List<Integer> getTopFilmsId(Integer count);
}
