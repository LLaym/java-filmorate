package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    int save(Review review);

    boolean update(Review review);

    Optional<Review> findById(int reviewId);

    List<Review> findAll(int limit);

    List<Review> findAllByFilmId(int reviewId, int limit);

    boolean delete(int reviewId);

    Integer findReviewer(int reviewId);

    boolean notExists(Integer id);
}
