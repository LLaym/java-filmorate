package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    // Обновлять нужно только поле is_positive, иначе тесты не проходят

    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("id");

        int filmId = review.getFilmId();
        int userId = review.getUserId();
        String content = review.getContent();
        boolean isPositive = review.getIsPositive();
        int useful = review.getUseful();

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("film_id", filmId);
        parameters.put("user_id", userId);
        parameters.put("content", content);
        parameters.put("is_positive", isPositive);
        parameters.put("useful", useful);

        return simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    @Override
    public boolean update(Review review) {
        String updateQuery = "UPDATE reviews "
                + "SET content = ?"
                + ", is_positive = ? "
                + "WHERE id = ?";

        int id = review.getReviewId();
        String content = review.getContent();
        boolean isPositive = review.getIsPositive();

        return jdbcTemplate.update(updateQuery, content, isPositive, id) == 1;
    }

    @Override
    public Optional<Review> findById(int reviewId) {
        String findByIdQuery = "SELECT * " +
                "FROM reviews " +
                "WHERE id = ?";

        return jdbcTemplate.query(findByIdQuery, ((rs, rowNum) -> makeReview(rs)), reviewId)
                .stream()
                .findFirst();
    }

    @Override
    public List<Review> findAll(int limit) {
        String findAllQuery = "SELECT * " +
                "FROM reviews " +
                "ORDER BY useful DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(findAllQuery, ((rs, rowNum) -> makeReview(rs)), limit);
    }

    @Override
    public List<Review> findAllByFilmId(int reviewId, int limit) {
        String findAllByFilmIdQuery = "SELECT * " +
                "FROM reviews " +
                "WHERE film_id = ? " +
                "ORDER BY useful DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(findAllByFilmIdQuery, ((rs, rowNum) -> makeReview(rs)), reviewId, limit);
    }

    @Override
    public boolean delete(int reviewId) {
        String deleteQuery = "DELETE FROM reviews WHERE id = ?";

        return jdbcTemplate.update(deleteQuery, reviewId) == 1;
    }

    public Integer findUserId(int reviewId) {
        String findUserIdQuery = "SELECT user_id FROM reviews WHERE id = ?";

        return jdbcTemplate.queryForObject(findUserIdQuery, Integer.class, reviewId);
    }

    private Review makeReview(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int filmId = rs.getInt("film_id");
        int userId = rs.getInt("user_id");
        String content = rs.getString("content");
        boolean isPositive = rs.getBoolean("is_positive");
        int useful = rs.getInt("useful");

        return Review.builder()
                .reviewId(id)
                .filmId(filmId)
                .userId(userId)
                .content(content)
                .isPositive(isPositive)
                .useful(useful)
                .build();
    }
}
