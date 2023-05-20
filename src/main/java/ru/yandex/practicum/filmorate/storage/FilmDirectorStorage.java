package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmDirector;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmDirectorStorage {
    void save(int filmId, int directorId);

    boolean deleteAllByFilmId(int filmId);

    List<FilmDirector> getAllByFilmId(int filmId);

    List<FilmDirector> getAllByDirector(Integer directorId);
}
