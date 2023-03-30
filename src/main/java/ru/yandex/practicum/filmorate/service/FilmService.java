package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

@Slf4j
@Service
public class FilmService {
    private FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        validate(film);
        log.debug("Выполнен POST /films. Фильм: {}, " +
                "количество фильмов в базе: {}", film, filmStorage.getAllFilms().size());
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validate(film);
        log.debug("Выполнен PUT /films. Фильм: {}, " +
                "количество фильмов в базе: {}", film, filmStorage.getAllFilms().size());
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> findAllFilms() {
        log.info("Возвращен список всех фильмов");
        return filmStorage.getAllFilms();
    }

    private void validate(Film film) throws ValidationException {
        RuntimeException e = null;
        boolean isNewFilm = film.getId() == 0;
        if (isNewFilm) {
            if (film.getName() == null || film.getName().equals("")) {
                e = new ValidationException("Название не может быть пустым");
            } else if (film.getDescription().length() > 200) {
                e = new ValidationException("Максимальная длина описания — 200 символов");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
                e = new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
            } else if (film.getDuration() <= 0) {
                e = new ValidationException("Продолжительность фильма должна быть положительной");
            }
        } else {
            if (filmStorage.getAllFilms().stream().noneMatch(film1 -> film1.getId() == film.getId())) {
                e = new ValidationException("Фильма с таким id не существует");
            }
        }
        if (e != null) {
            log.warn(e.getMessage(), e);
            throw e;
        }
    }
}
