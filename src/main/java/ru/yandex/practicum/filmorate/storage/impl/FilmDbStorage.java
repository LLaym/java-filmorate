package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        String name = film.getName();
        String description = film.getDescription();
        String release_date = film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String duration = String.valueOf(film.getDuration());

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("description", description);
        parameters.put("release_date", release_date);
        parameters.put("duration", duration);

        Integer generatedId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();

        return getFilmById(generatedId);
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films " +
                "SET name = ?" +
                ", description = ?" +
                ", release_date = ?" +
                ", duration = ? " +
                "WHERE id = ?";

        String id = String.valueOf(film.getId());
        String name = film.getName();
        String description = film.getDescription();
        String release_date = film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String duration = String.valueOf(film.getDuration());

        jdbcTemplate.update(sql, name, description, release_date, duration, id);

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

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate release_date = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");

        Film film = Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(release_date)
                .duration(duration)
                .build();

        return film;
    }
}
