package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

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
    private final FilmDirectorStorage filmDirectorStorage;
    private final DirectorStorage directorStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage, FilmGenreStorage filmGenreStorage, GenreStorage genreStorage, FilmDirectorStorage filmDirectorStorage, DirectorStorage directorStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.genreStorage = genreStorage;
        this.filmDirectorStorage = filmDirectorStorage;
        this.directorStorage = directorStorage;
    }

    @Override
    public int save(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        String name = film.getName();
        String description = film.getDescription();
        String releaseDate = film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String duration = String.valueOf(film.getDuration());
        String mpa = String.valueOf(film.getMpa().getId());

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("description", description);
        parameters.put("release_date", releaseDate);
        parameters.put("duration", duration);
        parameters.put("mpa_id", mpa);

        int generatedId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();

        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> filmGenreStorage.save(generatedId, genre.getId()));
        }
        if (film.getDirectors() != null) {
            film.getDirectors().forEach(director -> filmDirectorStorage.save(generatedId, director.getId()));
        }

        return generatedId;
    }

    @Override
    public void update(Film film) {
        String updateQuery = "UPDATE films "
                + "SET name = ?"
                + ", description = ?"
                + ", release_date = ?"
                + ", duration = ? "
                + ", mpa_id = ? "
                + "WHERE id = ?";
        String id = String.valueOf(film.getId());
        String name = film.getName();
        String description = film.getDescription();
        String releaseDate = film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String duration = String.valueOf(film.getDuration());
        String mpa = String.valueOf(film.getMpa().getId());

        filmGenreStorage.deleteAllByFilmId(film.getId());
        if (film.getGenres() != null) {
            film.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .forEach(genreId -> filmGenreStorage.save(film.getId(), genreId));
        }
        filmDirectorStorage.deleteAllByFilmId(film.getId());
        if (film.getDirectors() != null) {
            film.getDirectors()
                    .stream()
                    .map(Director::getId)
                    .forEach(directorId -> filmDirectorStorage.save(film.getId(), directorId));
        }

        jdbcTemplate.update(updateQuery, name, description, releaseDate, duration, mpa, id);
    }

    @Override
    public Optional<Film> findById(int filmId) {
        String findByIdQuery = "SELECT * FROM films WHERE id = ?";

        return jdbcTemplate.query(findByIdQuery, ((rs, rowNum) -> makeFilm(rs)), filmId)
                .stream()
                .findFirst();
    }

    @Override
    public List<Film> findAll() {
        String findAllQuery = "SELECT * FROM films";

        return jdbcTemplate.query(findAllQuery, ((rs, rowNum) -> makeFilm(rs)));
    }

    @Override
    public void deleteById(int filmId) {
        String deleteByIdQuery = "DELETE FROM films WHERE id = ?";

        jdbcTemplate.update(deleteByIdQuery, filmId);
    }

    @Override
    public List<Film> findAllByNameSubstring(String query) {
        String findAllByNameSubstringQuery = "SELECT * FROM films WHERE LOWER(name) LIKE LOWER(?)";

        return jdbcTemplate.query(findAllByNameSubstringQuery, ((rs, rowNum) -> makeFilm(rs)), "%" + query + "%");
    }

    @Override
    public boolean notExists(Integer id) {
        String notExistsQuery = "SELECT COUNT(*) FROM films WHERE id = ?";

        Integer count = jdbcTemplate.queryForObject(notExistsQuery, Integer.class, id);

        return count == null || count == 0;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = mpaStorage.findById(rs.getInt("mpa_id")).orElse(null);
        Set<Genre> genres = filmGenreStorage.findAllByFilmId(id)
                .stream()
                .map(FilmGenre::getGenreId)
                .map(genreStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        List<Director> directors = filmDirectorStorage.findAllByFilmId(id)
                .stream()
                .map(FilmDirector::getDirectorId)
                .map(directorStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return Film.builder()
                .id(id).name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(mpa)
                .genres(genres)
                .directors(directors)
                .build();
    }
}
