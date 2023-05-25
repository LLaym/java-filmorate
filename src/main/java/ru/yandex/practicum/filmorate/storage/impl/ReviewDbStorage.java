package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    // Обновлять нужно только поле is_positive, иначе тесты не проходят
    private final String updateSql = "UPDATE reviews "
            + "SET content = ?"
            + ", is_positive = ? "
            + "WHERE id = ?";
    private final String deleteSql = "DELETE FROM reviews WHERE id = ?";
    private final String getByIdSql = "SELECT * " +
            "FROM reviews " +
            "WHERE id = ?";
    private final String getAllSql = "SELECT * " +
            "FROM reviews " +
            "ORDER BY useful DESC " +
            "LIMIT ?";
    private final String getAllByFilmIdSql = "SELECT * " +
            "FROM reviews " +
            "WHERE film_id = ? " +
            "ORDER BY useful DESC " +
            "LIMIT ?";

    private final String getUserIdSql = "SELECT user_id FROM reviews WHERE id = ?";

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
        int id = review.getReviewId();
        String content = review.getContent();
        boolean isPositive = review.getIsPositive();

        return jdbcTemplate.update(updateSql, content, isPositive, id) == 1;
    }

    @Override
    public Optional<Review> getById(int reviewId) {
        return jdbcTemplate.query(getByIdSql, ((rs, rowNum) -> makeReview(rs)), reviewId)
                .stream()
                .findFirst();
    }

    @Override
    public List<Review> getAll(int limit) {
        return jdbcTemplate.query(getAllSql, ((rs, rowNum) -> makeReview(rs)), limit);
    }

    @Override
    public List<Review> getAllByFilmId(int reviewId, int limit) {
        return jdbcTemplate.query(getAllByFilmIdSql, ((rs, rowNum) -> makeReview(rs)), reviewId, limit);
    }

    @Override
    public boolean delete(int reviewId) {
        return jdbcTemplate.update(deleteSql, reviewId) == 1;
    }

    public Integer getUserId(int reviewId) {
        return jdbcTemplate.queryForObject(getUserIdSql, Integer.class, reviewId);
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
