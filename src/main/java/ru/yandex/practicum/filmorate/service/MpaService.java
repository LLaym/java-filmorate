package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> findAllMpas() {
        log.info("Возвращен список всех Mpa рейтингов");

        return mpaStorage.getAllMpa();
    }

    public Mpa findMpaById(Integer id) {
        Mpa mpa = mpaStorage.getMpaById(id)
                .orElseThrow(() -> new MpaNotFoundException("Mpa рейтинг с идентификатором " + id + " не найден"));

        log.info("Получен Mpa рейтинг: {}", mpa);

        return mpa;
    }
}
