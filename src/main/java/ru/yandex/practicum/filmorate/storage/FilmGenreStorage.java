package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreStorage {
    void save(int filmId, int genreId);

    boolean deleteAllByFilmId(int filmId);

    List<FilmGenre> findAllByFilmId(int filmId);
}
