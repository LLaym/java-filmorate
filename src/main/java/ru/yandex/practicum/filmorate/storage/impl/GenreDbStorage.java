package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final String getByIdSql = "SELECT * FROM genres WHERE id = ?";
    private final String getAllSql = "SELECT * FROM genres ORDER BY id";

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> getById(int genreId) {
        return jdbcTemplate.query(getByIdSql, ((rs, rowNum) -> makeGenre(rs)), genreId)
                .stream().findFirst();
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(getAllSql, ((rs, rowNum) -> makeGenre(rs)));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }
}
