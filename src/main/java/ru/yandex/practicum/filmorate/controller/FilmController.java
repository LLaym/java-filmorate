package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static int nextId;
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film createUser(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(++nextId);
        films.put(film.getId(), film);
        log.debug("Выполнен POST /films. Фильм: {}, " +
                "количество фильмов в базе: {}", film, films.size());
        return film;
    }

    @PutMapping
    public Film updateUser(@Valid @RequestBody Film film) {
        validate(film);
        films.put(film.getId(), film);
        log.debug("Выполнен PUT /films. Фильм: {}, " +
                "количество фильмов в базе: {}", film, films.size());
        return film;
    }

    @GetMapping
    public Collection<Film> findAllUsers() {
        log.info("Выполнен GET /films");
        return films.values();
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
            if (!films.containsKey(film.getId())) {
                e = new ValidationException("Фильма с таким id не существует");
            }
        }
        if (e != null) {
            log.warn(e.getMessage(), e);
            throw e;
        }
    }
}
