package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sql = "SELECT * FROM genres WHERE id = ?";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeGenre(rs)), id)
                .stream().findFirst().get();
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeGenre(rs)));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new Genre(id, name);
    }
}
