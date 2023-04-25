package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User saveUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        String email = user.getEmail();
        String login = user.getLogin();
        String name = user.getName();
        String birthday = user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("login", login);
        parameters.put("name", name);
        parameters.put("birthday", birthday);

        int generatedId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();

        return getUserById(generatedId).get();
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users " +
                "SET email = ?" +
                ", login = ?" +
                ", name = ?" +
                ", birthday = ? " +
                "WHERE id = ?";

        String id = String.valueOf(user.getId());
        String email = user.getEmail();
        String login = user.getLogin();
        String name = user.getName();
        String birthday = user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        jdbcTemplate.update(sql, email, login, name, birthday, id);

        return getUserById(user.getId()).get();
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)));
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)), id)
                .stream().findFirst();
    }

    @Override
    public void saveFriendship(Integer id, Integer friendId) {
        String sql = "INSERT INTO friendships (first_user_id, second_user_id) VALUES (?, ?)";
        String sql2 = "INSERT INTO friendships (first_user_id, second_user_id) VALUES (?, ?)";

        jdbcTemplate.update(sql, id, friendId);
        jdbcTemplate.update(sql2, friendId, id);
    }

    @Override
    public void removeFriendship(Integer id, Integer friendId) {
        String sql = "DELETE FROM friendships WHERE first_user_id = ? AND second_user_id = ?";
        String sql2 = "DELETE FROM friendships WHERE first_user_id = ? AND second_user_id = ?";

        jdbcTemplate.update(sql, id, friendId);
        jdbcTemplate.update(sql2, friendId, id);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        Set<Integer> friends = getUserFriends(id);

        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .friends(friends)
                .build();
    }

    private Set<Integer> getUserFriends(Integer userId) {
        String sql = "SELECT second_user_id FROM friendships WHERE first_user_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

        Set<Integer> friends = new HashSet<>();

        while (rowSet.next()) {
            friends.add(rowSet.getInt("second_user_id"));
        }

        return friends;
    }
}
