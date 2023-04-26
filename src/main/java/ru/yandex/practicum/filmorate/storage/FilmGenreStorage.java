package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface FilmGenreStorage {
    void save(int filmId, int genreId);

    void deleteAllByFilmId(int filmId);

    List<Integer> getAllByFilmId(int filmId);
}
