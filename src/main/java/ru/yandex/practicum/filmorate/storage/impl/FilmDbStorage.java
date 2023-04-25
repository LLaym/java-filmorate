package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Film saveFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        String name = film.getName();
        String description = film.getDescription();
        String release_date = film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String duration = String.valueOf(film.getDuration());
        String mpa = String.valueOf(film.getMpa().getId());

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("description", description);
        parameters.put("release_date", release_date);
        parameters.put("duration", duration);
        parameters.put("mpa_id", mpa);

        int generatedId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();

        return getFilmById(generatedId);
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films " +
                "SET name = ?" +
                ", description = ?" +
                ", release_date = ?" +
                ", duration = ? " +
                ", mpa_id = ? " +
                "WHERE id = ?";

        String id = String.valueOf(film.getId());
        String name = film.getName();
        String description = film.getDescription();
        String release_date = film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String duration = String.valueOf(film.getDuration());
        String mpa = String.valueOf(film.getMpa().getId());

        jdbcTemplate.update(sql, name, description, release_date, duration, mpa, id);

        return getFilmById(film.getId());
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT * FROM films";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)));
    }

    @Override
    public Film getFilmById(Integer id) {
        String sql = "SELECT * FROM films WHERE id = ?";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)), id)
                .stream().findFirst().orElse(null);
    }

    @Override
    public Film saveLike(Integer id, Integer userId) {
        String sql = "INSERT INTO film_like (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.update(sql, id, userId);

        return getFilmById(id);
    }

    @Override
    public Film removeLike(Integer id, Integer userId) {
        String sql = "DELETE FROM film_like WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sql, id, userId);

        return getFilmById(id);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate release_date = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = mpaStorage.getMpaById(rs.getInt("mpa_id")).orElse(null);
        Set<Integer> likes = getFilmLikes(id);

        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(release_date)
                .duration(duration)
                .mpa(mpa)
                .likes(likes)
                .build();
    }

    private Set<Integer> getFilmLikes(Integer filmId) {
        String sql = "SELECT user_id FROM film_like WHERE film_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, filmId);

        Set<Integer> likes = new HashSet<>();

        while (rowSet.next()) {
            likes.add(rowSet.getInt("user_id"));
        }

        return likes;
    }
}
