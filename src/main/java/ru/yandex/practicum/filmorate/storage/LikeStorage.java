package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;

public interface LikeStorage {
    Collection<Like> saveLike(Integer filmId, Integer userId);

    Collection<Like> removeLike(Integer filmId, Integer userId);

    Collection<Integer> getTopFilms(Integer count);
}
