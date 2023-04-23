package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private FilmStorage filmStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        log.info("Добавлен фильм: {}", film);
        return filmStorage.saveFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Обновлён фильм: {}", film);
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> findAllFilms() {
        log.info("Возвращен список всех фильмов");
        return filmStorage.getAllFilms();
    }

    public Film findFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);

        log.info("Получен фильм: {}", film);
        return film;
    }

    public Film likeFilm(Integer id, Integer userId) {
        Film film = filmStorage.saveLike(id, userId);

        log.info("Поставлен лайк фильму: {}", film);
        return film;
    }

    public Film dislikeFilm(Integer id, Integer userId) {
        Film film = filmStorage.getFilmById(id);
        film.getLikes().remove(userId);

        log.info("Убран лайк у фильма: {}", film);
        return film;
    }

    public Collection<Film> findTopFilms(Integer count) {
        List<Film> sortedFilms = filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .collect(Collectors.toList());

        log.info("Возвращен топ {} фильмов", count);
        if (sortedFilms.size() > count) {
            return sortedFilms.subList(0, count);
        } else {
            return sortedFilms;
        }
    }
}