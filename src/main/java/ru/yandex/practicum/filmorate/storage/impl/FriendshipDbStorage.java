package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;
    private final String saveSql = "INSERT INTO friendships (first_user_id, second_user_id) VALUES (?, ?)";
    private final String deleteSql = "DELETE FROM friendships WHERE first_user_id = ? AND second_user_id = ?";
    private final String getAllByUserIdSql = "SELECT * " +
            "FROM friendships " +
            "WHERE first_user_id = ?";

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int userId, int friendId) {
        jdbcTemplate.update(saveSql, userId, friendId);
    }

    @Override
    public boolean delete(int userId, int friendId) {
        return jdbcTemplate.update(deleteSql, userId, friendId) == 1;
    }

    @Override
    public List<Friendship> findAllByUserId(int userId) {
        return jdbcTemplate.query(getAllByUserIdSql, ((rs, rowNum) -> makeFriendship(rs)), userId);
    }

    private Friendship makeFriendship(ResultSet rs) throws SQLException {
        return new Friendship(rs.getInt("first_user_id"), rs.getInt("second_user_id"));
    }
}