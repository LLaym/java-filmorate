package ru.yandex.practicum.filmorate.storage.impl;

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
    private final String saveSql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
    private final String deleteSql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private final String getPopularFilmsIdsSql = "SELECT id " +
            "FROM (SELECT films.id, COUNT(user_id) AS score" +
            " FROM films" +
            " LEFT JOIN likes l on films.id = l.film_id" +
            " GROUP BY films.id" +
            " ORDER BY score DESC)" +
            " LIMIT ?;";

    private final String getSimilarUserIdSql = "SELECT user_id" +
            " FROM likes" +
            " WHERE film_id IN" +
            " (SELECT film_id FROM likes WHERE user_id = ?)" +
            " AND user_id <> ?" +
            " GROUP BY user_id" +
            " ORDER BY COUNT(film_id) DESC" +
            " LIMIT 1;";

    private final String getRecommendFilmsIdsSql = "SELECT film_id" +
            " FROM likes" +
            " WHERE  user_id = ?" +
            " AND film_id NOT IN" +
            " (SELECT film_id FROM likes WHERE user_id = ?);";

    private final String getCommonFilmsIdsSql = "SELECT film_id " +
            " FROM likes" +
            " WHERE film_id IN" +
            " ((SELECT film_id FROM likes WHERE user_id = ?)" +
            " INTERSECT" +
            " (SELECT film_id FROM likes WHERE user_id = ?))" +
            " GROUP BY film_id" +
            " ORDER BY COUNT(user_id) DESC;";

    private final String getAllByFilmSql = "SELECT * FROM likes WHERE film_id = ?";

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean save(int filmId, int userId) {
        return jdbcTemplate.update(saveSql, filmId, userId) > 1;
    }

    @Override
    public boolean delete(int filmId, int userId) {
        return jdbcTemplate.update(deleteSql, filmId, userId) > 1;
    }

    @Override
    public List<Integer> getPopularFilmsIds(int count) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(getPopularFilmsIdsSql, count);
        List<Integer> top = new ArrayList<>();

        while (rowSet.next()) {
            top.add(rowSet.getInt("id"));
        }
        return top;
    }

    @Override
    public List<Integer> getRecommendFilmsIds(int userId) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(getSimilarUserIdSql, userId, userId);
        if (!rowSet.next()) {
            return new ArrayList<>();
        }
        Integer similarUserId = rowSet.getInt("user_id");

        rowSet = jdbcTemplate.queryForRowSet(getRecommendFilmsIdsSql, similarUserId, userId);
        List<Integer> recommendFilms = new ArrayList<>();
        while (rowSet.next()) {
            recommendFilms.add(rowSet.getInt("film_id"));
        }
        return recommendFilms;
    }

    @Override
    public List<Integer> getCommonFilmsIds(int userId, int friendId) {
        return jdbcTemplate.queryForList(getCommonFilmsIdsSql, Integer.class, userId, friendId);
    }

    @Override
    public List<Like> getAllByFilmId(int filmId) {
        return jdbcTemplate.query(getAllByFilmSql, ((rs, rowNum) -> makeLike(rs)), filmId);
    }

    private Like makeLike(ResultSet rs) throws SQLException {
        return new Like(rs.getInt("film_id"), rs.getInt("user_id"));
    }
}
