package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.ReviewRatingStorage;

@Repository
public class ReviewRatingDbStorage implements ReviewRatingStorage {
    private final JdbcTemplate jdbcTemplate;

    public ReviewRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int reviewId, int userId, boolean isLiked) {
        String saveQuery = "INSERT INTO review_ratings (review_id, user_id, is_liked) VALUES (?, ?, ?)";
        String updateReviewQuery = "UPDATE reviews SET useful = useful + ? WHERE id = ?";

        if (jdbcTemplate.update(saveQuery, reviewId, userId, isLiked) == 1) {
            jdbcTemplate.update(updateReviewQuery, isLiked ? 1 : -1, reviewId);
        }
    }

    @Override
    public void delete(int reviewId, int userId, boolean isLiked) {
        String deleteQuery = "DELETE FROM review_ratings WHERE review_id = ? AND user_id = ? AND is_liked = ?";
        String updateReviewQuery = "UPDATE reviews SET useful = useful + ? WHERE id = ?";

        if (jdbcTemplate.update(deleteQuery, reviewId, userId, isLiked) == 1) {
            jdbcTemplate.update(updateReviewQuery, isLiked ? -1 : 1, reviewId);
        }
    }
}
