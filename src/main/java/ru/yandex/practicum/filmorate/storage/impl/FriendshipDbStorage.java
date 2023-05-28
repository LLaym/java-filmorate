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

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int userId, int friendId) {
        String saveQuery = "INSERT INTO friendships (first_user_id, second_user_id) VALUES (?, ?)";

        jdbcTemplate.update(saveQuery, userId, friendId);
    }

    @Override
    public boolean delete(int userId, int friendId) {
        String deleteQuery = "DELETE FROM friendships WHERE first_user_id = ? AND second_user_id = ?";

        return jdbcTemplate.update(deleteQuery, userId, friendId) == 1;
    }

    @Override
    public List<Friendship> findAllByUserId(int userId) {
        String findAllByUserIdQuery = "SELECT * " +
                "FROM friendships " +
                "WHERE first_user_id = ?";

        return jdbcTemplate.query(findAllByUserIdQuery, ((rs, rowNum) -> makeFriendship(rs)), userId);
    }

    private Friendship makeFriendship(ResultSet rs) throws SQLException {
        return new Friendship(rs.getInt("first_user_id"), rs.getInt("second_user_id"));
    }
}