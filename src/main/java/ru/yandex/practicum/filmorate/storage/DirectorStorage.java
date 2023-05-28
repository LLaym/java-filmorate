package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    int save(Director director);

    boolean update(Director director);

    Optional<Director> findById(int directorId);

    List<Director> findAll();

    boolean delete(int directorId);

    List<Director> findAllByNameSubstring(String query);
}