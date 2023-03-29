package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Выполнен POST /films. Фильм: {}, " +
                "количество фильмов в базе: {}", film, filmStorage.findAllFilms().size());
        validate(film);
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Выполнен PUT /films. Фильм: {}, " +
                "количество фильмов в базе: {}", film, filmStorage.findAllFilms().size());
        validate(film);
        return filmStorage.updateFilm(film);
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        log.info("Выполнен GET /films");
        return filmStorage.findAllFilms();
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
            if (filmStorage.findAllFilms().stream().noneMatch(film1 -> film1.getId() == film.getId())) {
                e = new ValidationException("Фильма с таким id не существует");
            }
        }
        if (e != null) {
            log.warn(e.getMessage(), e);
            throw e;
        }
    }
}
