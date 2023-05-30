package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        Director createdDirector = directorStorage.findById(generatedId).orElse(null);

        log.info("Добавлен режиссёр: {}", createdDirector);
        return createdDirector;
    }

    public Director updateDirector(Director director) {
        Integer directorId = director.getId();

        if (directorStorage.notExists(directorId)) {
            throw new NotFoundException("Режиссёр с id " + directorId + " не найден");
        }

        directorStorage.update(director);
        Director updatedDirector = directorStorage.findById(directorId).orElse(null);

        log.info("Обновлён режиссёр: {}", updatedDirector);
        return updatedDirector;
    }

    public List<Director> getAllDirectors() {
        List<Director> directors = directorStorage.findAll();

        log.info("Возвращен список всех режиссёров");
        return directors;
    }

    public Director getDirectorById(Integer directorId) {
        Director director = directorStorage.findById(directorId)
                .orElseThrow(() -> new NotFoundException("Режиссёр с id " + directorId + " не найден"));

        log.info("Получен режиссёр: {}", director);
        return director;
    }

    public void deleteDirector(Integer directorId) {
        if (directorStorage.notExists(directorId)) {
            throw new NotFoundException("Режиссёр с id " + directorId + " не найден");
        }

        directorStorage.delete(directorId);
        log.info("Режиссёр с id {} удалён.", directorId);
    }
}
