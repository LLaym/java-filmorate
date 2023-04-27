package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
    }

    public Film createFilm(Film film) {
        int generatedId = filmStorage.save(film);
        Film createdFilm = filmStorage.getById(generatedId).get();

        log.info("Добавлен фильм: {}", createdFilm);
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        filmStorage.update(film);
        Film updatedFilm = filmStorage.getById(film.getId()).get();

        log.info("Обновлён фильм: {}", film);
        return updatedFilm;
    }

    public List<Film> findAllFilms() {
        log.info("Возвращен список всех фильмов");
        return filmStorage.getAll();
    }

    public Film findFilmById(Integer filmId) {
        Film film = filmStorage.getById(filmId)
                .orElseThrow(() -> new FilmNotFoundException("Фильм с id " + filmId + " не найден"));

        log.info("Получен фильм: {}", film);
        return film;
    }

    public boolean likeFilm(Integer filmId, Integer userId) {
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
        return likeStorage.save(filmId, userId);
    }

    public boolean dislikeFilm(Integer filmId, Integer userId) {
        log.info("Пользователь с id {} убрал лайк с фильма с id {}", userId, filmId);
        return likeStorage.delete(filmId, userId);
    }

    public List<Film> findTopFilms(Integer count) {
        List<Film> topFilms = likeStorage.getPopularFilmsIds(count)
                .stream()
                .map(filmStorage::getById)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Возвращен топ фильмов: {} ", topFilms);
        return topFilms;
    }
}