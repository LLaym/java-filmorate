package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Integer save(Film film);

    Boolean update(Film film);

    Film getById(int filmId);

    List<Film> getAll();
}