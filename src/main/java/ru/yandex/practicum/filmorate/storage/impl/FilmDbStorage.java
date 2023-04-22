package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final GenreStorage genreStorage;
    private final MpaRatingStorage mpaRatingStorage;
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(GenreStorage genreStorage, MpaRatingStorage mpaRatingStorage, JdbcTemplate jdbcTemplate) {
        this.genreStorage = genreStorage;
        this.mpaRatingStorage = mpaRatingStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration)" +
                " VALUES (?, ?, ?, ?)";

        String name = film.getName();
        String description = film.getDescription();
        String release_date = film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String duration = String.valueOf(film.getDuration());
//        String mpa_rating_id = String.valueOf(film.getMpa().getId());

        jdbcTemplate.update(sql, name, description, release_date, duration);

        // TODO Здесь нужно вернуть фильм из базы данных. То есть return getFilmById()
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films " +
                "SET name = ?, description = ?, release_date = ?, duration = ? " +
                "WHERE id = ?";

        String id = String.valueOf(film.getId());
        String name = film.getName();
        String description = film.getDescription();
        String release_date = film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String duration = String.valueOf(film.getDuration());
//        String mpa_rating_id = String.valueOf(film.getMpa().getId());

        jdbcTemplate.update(sql, name, description, release_date, duration, id);

        // TODO Здесь нужно вернуть фильм из базы данных. То есть return getFilmById()
        return film;
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
                .stream().findFirst().get();
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate release_date = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
//        MpaRating mpaRating = mpaRatingStorage.getMpaRatingById(rs.getInt("mpa_rating_id"));
//        List<Genre> genres = (List<Genre>) genreStorage.getGenresByFilmId(id);

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
