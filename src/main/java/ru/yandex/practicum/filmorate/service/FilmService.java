package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private static int nextId;
    private FilmStorage filmStorage;

    public Film createFilm(Film film) {
        Validator.validateFilm(film);
        Film filmWithId = film.toBuilder().id(++nextId).build();
        log.info("Добавлен фильм: {}", filmWithId);
        return filmStorage.createFilm(filmWithId);
    }

    public Film updateFilm(Film film) {
        Validator.validateFilm(film);
        log.info("Обновлён фильм: {}", film);
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> findAllFilms() {
        log.info("Возвращен список всех фильмов");
        return filmStorage.getAllFilms();
    }

    public Film findFilmById(Integer id) {
        Validator.validateFilmId(id);
        Film film = filmStorage.getFilmById(id);
        log.info("Получен фильм: {}", film);
        return film;
    }

    public Film likeFilm(Integer id, Integer userId) {
        Validator.validateFilmId(id);
        Validator.validateUserId(userId);
        Film film = filmStorage.getFilmById(id);
        film.getLikes().add(userId);
        log.info("Поставлен лайк фильму: {}", film);
        return film;
    }

    public Film dislikeFilm(Integer id, Integer userId) {
        Validator.validateFilmId(id);
        Validator.validateUserId(userId);
        Film film = filmStorage.getFilmById(id);
        film.getLikes().remove(userId);
        log.info("Убран лайк у фильма: {}", film);
        return film;
    }

    public Collection<Film> findTopFilms(Integer count) {
        List<Film> sortedFilms = filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .collect(Collectors.toList());

        if (sortedFilms.size() > count) {
            return sortedFilms.subList(0, count);
        } else {
            return sortedFilms;
        }
    }
}
