package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", director.getName());

        int generatedId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();

        return generatedId;
    }

    @Override
    public boolean update(Director director) {
        String updateQuery = "UPDATE directors SET name = ? WHERE id = ?";
        String id = String.valueOf(director.getId());
        String name = director.getName();

        return jdbcTemplate.update(updateQuery, name, id) == 1;
    }

    @Override
    public Optional<Director> findById(int directorId) {
        String findByIdQuery = "SELECT * FROM directors WHERE id = ?";

        return jdbcTemplate.query(findByIdQuery, ((rs, rowNum) -> makeDirector(rs)), directorId)
                .stream()
                .findFirst();
    }

    @Override
    public List<Director> findAll() {
        String findAllQuery = "SELECT * FROM directors";

        return jdbcTemplate.query(findAllQuery, ((rs, rowNum) -> makeDirector(rs)));
    }

    @Override
    public boolean delete(int directorId) {
        String deleteQuery = "DELETE FROM directors WHERE id = ?";

        return jdbcTemplate.update(deleteQuery, directorId) == 1;
    }

    @Override
    public List<Director> findAllByNameSubstring(String query) {
        String findAllByNameSubstringQuery = "SELECT * FROM directors WHERE LOWER(name) LIKE LOWER(?)";

        return jdbcTemplate.query(findAllByNameSubstringQuery, ((rs, rowNum) -> makeDirector(rs)), "%" + query + "%");
    }

    private Director makeDirector(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new Director(id, name);
    }
}
