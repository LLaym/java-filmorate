package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean save(int filmId, int userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

        return jdbcTemplate.update(sql, filmId, userId) >= 1;
    }

    @Override
    public boolean delete(int filmId, int userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

        return jdbcTemplate.update(sql, filmId, userId) >= 1;
    }

    @Override
    public List<Integer> getPopularFilmsIds(int count) {
        String sql = "SELECT id " +
                "FROM (SELECT films.id, COUNT(user_id) AS score" +
                " FROM films" +
                " LEFT JOIN likes l on films.id = l.film_id" +
                " GROUP BY films.id" +
                " ORDER BY score DESC)" +
                " LIMIT ?;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, count);

        List<Integer> top = new ArrayList<>();

        while (rowSet.next()) {
            top.add(rowSet.getInt("id"));
        }

        return top;
    }
}
