package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    int save(Film film);

    boolean update(Film film);

    Optional<Film> getById(int filmId);

    Optional<Film> deleteById(int filmId);

    List<Film> getAll();
}