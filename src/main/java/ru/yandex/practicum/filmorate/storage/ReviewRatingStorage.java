package ru.yandex.practicum.filmorate.storage;

public interface ReviewRatingStorage {
    boolean save(int reviewId, int userId, boolean isLiked);

    boolean delete(int reviewId, int userId, boolean isLiked);
}
