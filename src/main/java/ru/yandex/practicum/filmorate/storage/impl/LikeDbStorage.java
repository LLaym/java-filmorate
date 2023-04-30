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
    private final String SAVE_SQL = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
    private final String DELETE_SQL = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private final String GET_POPULAR_FILMS_IDS_SQL = "SELECT id " +
            "FROM (SELECT films.id, COUNT(user_id) AS score" +
            " FROM films" +
            " LEFT JOIN likes l on films.id = l.film_id" +
            " GROUP BY films.id" +
            " ORDER BY score DESC)" +
            " LIMIT ?;";

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean save(int filmId, int userId) {
        return jdbcTemplate.update(SAVE_SQL, filmId, userId) > 1;
    }

    @Override
    public boolean delete(int filmId, int userId) {
        return jdbcTemplate.update(DELETE_SQL, filmId, userId) > 1;
    }

    @Override
    public List<Integer> getPopularFilmsIds(int count) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(GET_POPULAR_FILMS_IDS_SQL, count);
        List<Integer> top = new ArrayList<>();

        while (rowSet.next()) {
            top.add(rowSet.getInt("id"));
        }
        return top;
    }
}
