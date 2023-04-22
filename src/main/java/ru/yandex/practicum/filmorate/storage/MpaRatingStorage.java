package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;

public interface MpaRatingStorage {
    MpaRating getMpaRatingById(Integer id);

    Collection<MpaRating> getAllMpaRatings();
}
