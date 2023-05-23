package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final String updateSql = "UPDATE users " +
            "SET email = ?" +
            ", login = ?" +
            ", name = ?" +
            ", birthday = ? " +
            "WHERE id = ?";
    private final String getByIdSql = "SELECT * FROM users WHERE id = ?";
    private final String getAllSql = "SELECT * FROM users";
    private final String deleteByIdSql = "DELETE FROM users WHERE id = ?";

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(User user) {
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

        return simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    @Override
    public boolean update(User user) {
        String id = String.valueOf(user.getId());
        String email = user.getEmail();
        String login = user.getLogin();
        String name = user.getName();
        String birthday = user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return jdbcTemplate.update(updateSql, email, login, name, birthday, id) > 1;
    }

    @Override
    public Optional<User> getById(int userId) {
        return jdbcTemplate.query(getByIdSql, ((rs, rowNum) -> makeUser(rs)), userId)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<User> deleteById(int userId) {
        Optional<User> user = jdbcTemplate.query(getByIdSql, ((rs, rowNum) -> makeUser(rs)), userId)
                .stream()
                .findFirst();
        jdbcTemplate.update(deleteByIdSql, userId);
        return user;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(getAllSql, ((rs, rowNum) -> makeUser(rs)));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }
}
