package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeStorage {
    boolean save(int filmId, int userId);

    boolean delete(int filmId, int userId);

    List<Integer> getPopularFilmsIds(int count);

    List<Like> getAllByFilmId(int filmId);

    List<Integer> getRecommendFilmsIds(int userId);
}

