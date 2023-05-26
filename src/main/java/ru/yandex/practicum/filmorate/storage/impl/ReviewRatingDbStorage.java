package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.ReviewRatingStorage;

@Repository
public class ReviewRatingDbStorage implements ReviewRatingStorage {
    private final JdbcTemplate jdbcTemplate;
    private final String saveSql = "INSERT INTO review_ratings (review_id, user_id, is_liked) VALUES (?, ?, ?)";
    private final String updateReviewSql = "UPDATE reviews SET useful = useful + ? WHERE id = ?";
    private final String deleteSql = "DELETE FROM review_ratings WHERE review_id = ? AND user_id = ? AND is_liked = ?";

    public ReviewRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean save(int reviewId, int userId, boolean isLiked) {
        if (jdbcTemplate.update(saveSql, reviewId, userId, isLiked) == 1) {
            return jdbcTemplate.update(updateReviewSql, isLiked ? 1 : -1, reviewId) == 1;
        }
        return false;
    }

    @Override
    public boolean delete(int reviewId, int userId, boolean isLiked) {
        if (jdbcTemplate.update(deleteSql, reviewId, userId, isLiked) == 1) {
            return jdbcTemplate.update(updateReviewSql, isLiked ? -1 : 1, reviewId) == 1;
        }
        return false;
    }
}
