package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> filmGenreStorage.save(generatedId, genre.getId()));
        }

        return getFilmById(generatedId);
    }

    @Override
    public Film updateFilm(Film film) {
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

        filmGenreStorage.delete(film.getId());

        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> filmGenreStorage.save(film.getId(), genre.getId()));
        }

        jdbcTemplate.update(sql, name, description, release_date, duration, mpa, id);

        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM films";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)));
    }

    @Override
    public Film getFilmById(Integer id) {
        String sql = "SELECT * FROM films WHERE id = ?";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeFilm(rs)), id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate release_date = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = mpaStorage.getMpaById(rs.getInt("mpa_id")).orElse(null);

        List<Integer> filmGenres = filmGenreStorage.getGenresIdByFilmId(id);
        List<Genre> genres = new ArrayList<>();
        for (Integer filmGenre : filmGenres) {
            genres.add(genreStorage.getGenreById(filmGenre).get());
        }

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
