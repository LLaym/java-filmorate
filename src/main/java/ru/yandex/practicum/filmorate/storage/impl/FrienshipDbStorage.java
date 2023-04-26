package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

@Repository
public class FrienshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    public FrienshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Friendship save(int id, int friendId) {
        String sql = "INSERT INTO friendships (first_user_id, second_user_id) VALUES (?, ?)";
        String sql2 = "INSERT INTO friendships (first_user_id, second_user_id) VALUES (?, ?)";

        jdbcTemplate.update(sql, id, friendId);
        jdbcTemplate.update(sql2, friendId, id);

        return new Friendship(id, friendId);
    }

    @Override
    public boolean delete(int id, int friendId) {
        String sql = "DELETE FROM friendships WHERE first_user_id = ? AND second_user_id = ?";
        String sql2 = "DELETE FROM friendships WHERE first_user_id = ? AND second_user_id = ?";

        jdbcTemplate.update(sql, id, friendId);
        jdbcTemplate.update(sql2, friendId, id);

        return true;
    }
}
