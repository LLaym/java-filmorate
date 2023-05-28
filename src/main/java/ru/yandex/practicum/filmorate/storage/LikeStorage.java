package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeStorage {
    boolean save(int filmId, int userId);

    boolean delete(int filmId, int userId);

    List<Integer> findPopularFilmsIds(int count);

    List<Integer> findCommonFilmsIds(int userId, int friendId);

    List<Like> findAllByFilmId(int filmId);

    List<Integer> findRecommendFilmsIds(int userId);
}

