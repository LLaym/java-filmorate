package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmDirector;
import ru.yandex.practicum.filmorate.storage.FilmDirectorStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FilmDirectorDbStorage implements FilmDirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int filmId, int directorId) {
        String saveQuery = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?)";

        jdbcTemplate.update(saveQuery, filmId, directorId);
    }

    @Override
    public List<FilmDirector> findAllByFilmId(int filmId) {
        String findAllByFilmQuery = "SELECT * " +
                "FROM film_director " +
                "WHERE film_id = ?";

        return jdbcTemplate.query(findAllByFilmQuery, ((rs, rowNum) -> makeFilmDirector(rs)), filmId);
    }

    @Override
    public List<FilmDirector> findAllByDirector(Integer directorId) {
        String findAllByDirectorQuery = "SELECT * " +
                "FROM film_director " +
                "WHERE director_id = ?";

        return jdbcTemplate.query(findAllByDirectorQuery, ((rs, rowNum) -> makeFilmDirector(rs)), directorId);
    }

    @Override
    public boolean deleteAllByFilmId(int filmId) {
        String deleteQuery = "DELETE FROM film_director WHERE film_id = ?";

        return jdbcTemplate.update(deleteQuery, filmId) >= 1;
    }

    private FilmDirector makeFilmDirector(ResultSet rs) throws SQLException {
        return new FilmDirector(rs.getInt("film_id"), rs.getInt("director_id"));
    }
}
