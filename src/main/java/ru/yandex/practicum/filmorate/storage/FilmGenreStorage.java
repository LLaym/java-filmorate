package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface FilmGenreStorage {
    List<Integer> getGenresIdByFilmId(Integer filmId);

    void save(int filmId, Integer genreId);

    void delete(int filmId);
}
