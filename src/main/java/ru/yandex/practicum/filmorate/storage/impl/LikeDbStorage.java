package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    private final String findAllByFilmQuery = "SELECT * FROM likes WHERE film_id = ?";

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int filmId, int userId) {
        String saveQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(saveQuery, filmId, userId);
    }

    @Override
    public void delete(int filmId, int userId) {
        String deleteQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(deleteQuery, filmId, userId);
    }

    @Override
    public List<Integer> findPopularFilmsIds(int count) {
        String findPopularFilmsIdsQuery = "SELECT id " +
                "FROM (SELECT films.id, COUNT(user_id) AS score" +
                " FROM films" +
                " LEFT JOIN likes l on films.id = l.film_id" +
                " GROUP BY films.id" +
                " ORDER BY score DESC)" +
                " LIMIT ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(findPopularFilmsIdsQuery, count);
        List<Integer> top = new ArrayList<>();

        while (rowSet.next()) {
            top.add(rowSet.getInt("id"));
        }
        return top;
    }

    @Override
    public List<Integer> findRecommendFilmsIds(int userId) {
        String findSimilarUserIdQuery = "SELECT user_id" +
                " FROM likes" +
                " WHERE film_id IN" +
                " (SELECT film_id FROM likes WHERE user_id = ?)" +
                " AND user_id <> ?" +
                " GROUP BY user_id" +
                " ORDER BY COUNT(film_id) DESC" +
                " LIMIT 1;";
        String findRecommendFilmsIdsQuery = "SELECT film_id" +
                " FROM likes" +
                " WHERE  user_id = ?" +
                " AND film_id NOT IN" +
                " (SELECT film_id FROM likes WHERE user_id = ?);";

        try {
            Integer similarUserId = jdbcTemplate.queryForObject(findSimilarUserIdQuery, Integer.class, userId, userId);
            return jdbcTemplate.queryForList(findRecommendFilmsIdsQuery, Integer.class, similarUserId, userId);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Integer> findCommonFilmsIds(int userId, int friendId) {
        String findCommonFilmsIdsQuery = "SELECT film_id " +
                " FROM likes" +
                " WHERE film_id IN" +
                " ((SELECT film_id FROM likes WHERE user_id = ?)" +
                " INTERSECT" +
                " (SELECT film_id FROM likes WHERE user_id = ?))" +
                " GROUP BY film_id" +
                " ORDER BY COUNT(user_id) DESC;";

        return jdbcTemplate.queryForList(findCommonFilmsIdsQuery, Integer.class, userId, friendId);
    }

    @Override
    public List<Like> findAllByFilmId(int filmId) {
        return jdbcTemplate.query(findAllByFilmQuery, ((rs, rowNum) -> makeLike(rs)), filmId);
    }

    private Like makeLike(ResultSet rs) throws SQLException {
        return new Like(rs.getInt("film_id"), rs.getInt("user_id"));
    }
}
