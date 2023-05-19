package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    int save(Director director);

    boolean update(Director director);

    Optional<Director> getById(int directorId);

    List<Director> getAll();
    boolean delete(int directorId);
}