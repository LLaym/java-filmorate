package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public Collection<Genre> findAllGenres() {
        log.info("Возвращен список всех жанров");

        return genreStorage.getAllGenres();
    }

    public Genre findGenreById(Integer id) {
        Genre genre = genreStorage.getGenreById(id);

        log.info("Получен жанр: {}", genre);

        return genre;
    }
}