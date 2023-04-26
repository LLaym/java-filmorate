package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Repository
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Integer filmId, Integer userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void delete(Integer filmId, Integer userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sql, filmId, userId);
    }

//    @Override
//    public List<Integer> getTopFilmsId(Integer count) {
//        String sql = "SELECT film_id " +
//                "FROM (SELECT film_id, COUNT(user_id) AS score " +
//                "FROM likes " +
//                "GROUP BY film_id " +
//                "ORDER BY score DESC) " +
//                "LIMIT ?;";
//
//        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, count);
//
//        List<Integer> films = new ArrayList<>();
//
//        while (rowSet.next()) {
//            films.add(rowSet.getInt("film_id"));
//        }
//
//        return films;
//    }
}
