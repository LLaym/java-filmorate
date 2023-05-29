package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int filmId, int genreId) {
        String saveQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

        jdbcTemplate.update(saveQuery, filmId, genreId);
    }

    @Override
    public List<FilmGenre> findAllByFilmId(int filmId) {
        String findAllByFilmQuery = "SELECT * " +
                "FROM film_genre " +
                "WHERE film_id = ?";

        return jdbcTemplate.query(findAllByFilmQuery, ((rs, rowNum) -> makeFilmGenre(rs)), filmId);
    }

    @Override
    public void deleteAllByFilmId(int filmId) {
        String deleteQuery = "DELETE FROM film_genre WHERE film_id = ?";

        jdbcTemplate.update(deleteQuery, filmId);
    }

    private FilmGenre makeFilmGenre(ResultSet rs) throws SQLException {
        return new FilmGenre(rs.getInt("film_id"), rs.getInt("genre_id"));
    }
}
