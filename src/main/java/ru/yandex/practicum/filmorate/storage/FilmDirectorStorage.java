package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.util.List;

public interface FilmDirectorStorage {
    void save(int filmId, int directorId);

    boolean deleteAllByFilmId(int filmId);

    List<FilmDirector> findAllByFilmId(int filmId);

    List<FilmDirector> findAllByDirector(Integer directorId);
}
