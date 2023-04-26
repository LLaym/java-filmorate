package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final GenreStorage genreStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, LikeStorage likeStorage,
                       FilmGenreStorage filmGenreStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.genreStorage = genreStorage;
    }

    public Film createFilm(Film film) {
        int generatedId = filmStorage.save(film);

        Film createdFilm = filmStorage.getById(generatedId);

        log.info("Добавлен фильм: {}", createdFilm);
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        filmStorage.update(film);

        Film updatedFilm = filmStorage.getById(film.getId());

        log.info("Обновлён фильм: {}", film);
        return updatedFilm;
    }

    public List<Film> findAllFilms() {
        log.info("Возвращен список всех фильмов");
        return filmStorage.getAll();
    }

    public Film findFilmById(Integer id) {
        Film film = filmStorage.getById(id);

        log.info("Получен фильм: {}", film);
        return film;
    }

    public void likeFilm(Integer filmId, Integer userId) {
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
        likeStorage.save(filmId, userId);
    }

    public void dislikeFilm(Integer filmId, Integer userId) {
        log.info("Пользователь с id {} убрал лайк с фильма с id {}", userId, filmId);
        likeStorage.delete(filmId, userId);
    }

    public List<Film> findTopFilms(Integer count) {
        List<Film> topFilms = likeStorage.getPopularFilmsIds(count)
                .stream()
                .map(filmStorage::getById)
                .collect(Collectors.toList());

        log.info("Возвращен топ фильмов: ", topFilms);
        return topFilms;
    }
}