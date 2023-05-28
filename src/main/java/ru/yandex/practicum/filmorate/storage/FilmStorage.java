package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    int save(Film film);

    void update(Film film);

    Optional<Film> findById(int filmId);

    void deleteById(int filmId);

    List<Film> findAll();

    List<Film> findAllByNameSubstring(String query);
}