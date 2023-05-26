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
    private final String saveSql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
    private final String getAllByFilmSql = "SELECT * " +
            "FROM film_genre " +
            "WHERE film_id = ?";
    private final String deleteSql = "DELETE FROM film_genre WHERE film_id = ?";

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int filmId, int genreId) {
        jdbcTemplate.update(saveSql, filmId, genreId);
    }

    @Override
    public List<FilmGenre> getAllByFilmId(int filmId) {
        return jdbcTemplate.query(getAllByFilmSql, ((rs, rowNum) -> makeFilmGenre(rs)), filmId);
    }

    @Override
    public boolean deleteAllByFilmId(int filmId) {
        return jdbcTemplate.update(deleteSql, filmId) >= 1;
    }

    private FilmGenre makeFilmGenre(ResultSet rs) throws SQLException {
        return new FilmGenre(rs.getInt("film_id"), rs.getInt("genre_id"));
    }
}
