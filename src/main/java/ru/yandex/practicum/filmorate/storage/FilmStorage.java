package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film save(Film film);

    Film update(Film film);

    Film getById(int filmId);

    List<Film> getAll();
}