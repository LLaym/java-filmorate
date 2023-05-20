package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Slf4j
@Service
public class DirectorService {
    private final DirectorStorage directorStorage;

    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public Director createDirector(Director director) {
        int generatedId = directorStorage.save(director);
        Director createdDirector = directorStorage.getById(generatedId).orElse(null);

        log.info("Добавлен режиссёр: {}", createdDirector);
        return createdDirector;
    }

    public Director updateDirector(Director director) {
        directorStorage.update(director);
        Director updatedDirector = directorStorage.getById(director.getId()).orElse(null);

        log.info("Обновлён режиссёр: {}", updatedDirector);
        return updatedDirector;
    }

    public List<Director> findAllDirectors() {
        log.info("Возвращен список всех режиссёров");
        return directorStorage.getAll();
    }

    public Director findDirectorById(Integer directorId) {
        Director director = directorStorage.getById(directorId)
                .orElseThrow(() -> new DirectorNotFoundException("Режиссёр с id " + directorId + " не найден"));

        log.info("Получен режиссёр: {}", director);
        return director;
    }

    public boolean deleteDirector(Integer directorId) {
        log.info("Режиссёр с id {} удалён.", directorId);
        return directorStorage.delete(directorId);
    }
}
