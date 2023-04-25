package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Integer> getGenresIdByFilmId(Integer filmId) {
        String sql = "SELECT genre_id " +
                "FROM (SELECT genre_id " +
                "FROM film_genre " +
                "WHERE film_id = ?)";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, filmId);

        List<Integer> genres = new ArrayList<>();

        while (rowSet.next()) {
            genres.add(rowSet.getInt("genre_id"));
        }

        return genres;
    }

    @Override
    public void save(int filmId, Integer genreId) {
        String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

        jdbcTemplate.update(sql, filmId, genreId);

        // TODO в ответку должено что то возвращаться
    }

    @Override
    public void delete(int filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";

        jdbcTemplate.update(sql, filmId);

        // TODO в ответку должено что то возвращаться
    }
}
