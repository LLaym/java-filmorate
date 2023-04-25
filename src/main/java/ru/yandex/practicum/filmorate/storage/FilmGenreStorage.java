package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface FilmGenreStorage {
    List<Integer> getGenresByFilmId(Integer filmId);

    void save(int filmId, Integer genreId);

    void update(int filmId, Integer genreId);

    void delete(int filmId);
}
