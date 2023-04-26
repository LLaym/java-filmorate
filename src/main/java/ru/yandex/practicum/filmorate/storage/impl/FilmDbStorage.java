package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final GenreStorage genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage, FilmGenreStorage filmGenreStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film save(Film film) {
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

        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> filmGenreStorage.save(generatedId, genre.getId()));
        }

        return getById(generatedId);
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films "
                + "SET name = ?"
                + ", description = ?"
                + ", release_date = ?"
                + ", duration = ? "
                + ", mpa_id = ? "
                + "WHERE id = ?";

        String id = String.valueOf(film.getId());
        String name = film.getName();
        String description = film.getDescription();
        String release_date = film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String duration = String.valueOf(film.getDuration());
        String mpa = String.valueOf(film.getMpa().getId());

        jdbcTemplate.update(sql, name, description, release_date, duration, mpa, id);

        filmGenreStorage.deleteAllByFilmId(film.getId());
        if (film.getGenres() != null) {
            film.getGenres().stream()
                    .map(Genre::getId)
                    .forEach(genreId -> filmGenreStorage.save(film.getId(), genreId));
        }

        return getById(film.getId());
    }

    @Override
    public Film getById(int id) {
        String sql = "SELECT * FROM films WHERE id = ?";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)), id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException("Фильм с id " + id + " не найден"));
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM films";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate release_date = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = mpaStorage.getById(rs.getInt("mpa_id")).orElse(null);
        Set<Genre> genres = filmGenreStorage.getAllByFilmId(id)
                .stream()
                .map(FilmGenre::getGenreId)
                .map(genreStorage::getById)
                .map(Optional::get)
                .collect(Collectors.toSet());

        return Film.builder()
                .id(id).name(name)
                .description(description)
                .releaseDate(release_date)
                .duration(duration)
                .mpa(mpa)
                .genres(genres)
                .build();
    }
}
