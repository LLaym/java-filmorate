package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> findAllGenres() {
        log.info("Возвращен список всех жанров");
        return genreStorage.getAll();
    }

    public Genre findGenreById(Integer id) {
        Genre genre = genreStorage.getById(id)
                .orElseThrow(() -> new GenreNotFoundException("Жанр с идентификатором " + id + " не найден"));

        log.info("Получен жанр: {}", genre);
        return genre;
    }
}