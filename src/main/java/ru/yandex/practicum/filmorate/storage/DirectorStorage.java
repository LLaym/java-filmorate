package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    int save(Director director);

    void update(Director director);

    Optional<Director> findById(int directorId);

    List<Director> findAll();

    void delete(int directorId);

    List<Director> findAllByNameSubstring(String query);

    boolean existsById(Integer id);
}