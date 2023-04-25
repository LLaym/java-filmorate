package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;

public interface LikeStorage {
    Like saveLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    Collection<Integer> getTopFilms(Integer count);
}
