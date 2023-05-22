package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    int save(Review review);

    boolean update(Review review);

    Optional<Review> getById(int reviewId);

    List<Review> getAll(int limit);

    List<Review> getAllByFilmId(int reviewId, int limit);

    boolean delete(int reviewId);
}
