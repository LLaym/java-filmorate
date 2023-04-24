package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Repository
public class MpaDbStorage implements MpaStorage {
    @Override
    public Collection<Mpa> getAllMpas() {
        // TODO реализовать
        return null;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        // TODO реализовать
        return null;
    }
}
