package ru.yandex.practicum.filmorate.storage;

public interface ReviewRatingStorage {
    void save(int reviewId, int userId, boolean isLiked);

    void delete(int reviewId, int userId, boolean isLiked);
}
