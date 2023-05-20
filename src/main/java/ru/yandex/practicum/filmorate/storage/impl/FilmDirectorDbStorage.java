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
    private final String saveSql = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?)";
    private final String getAllByFilmSql = "SELECT * " +
            "FROM film_director " +
            "WHERE film_id = ?";
    private final String deleteSql = "DELETE FROM film_director WHERE film_id = ?";

    public FilmDirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(int filmId, int directorId) {
        jdbcTemplate.update(saveSql, filmId, directorId);
    }

    @Override
    public List<FilmDirector> getAllByFilmId(int filmId) {
        return jdbcTemplate.query(getAllByFilmSql, ((rs, rowNum) -> makeFilmDirector(rs)), filmId);
    }

    @Override
    public boolean deleteAllByFilmId(int filmId) {
        return jdbcTemplate.update(deleteSql, filmId) > 1;
    }

    private FilmDirector makeFilmDirector(ResultSet rs) throws SQLException {
        return new FilmDirector(rs.getInt("film_id"), rs.getInt("director_id"));
    }
}
