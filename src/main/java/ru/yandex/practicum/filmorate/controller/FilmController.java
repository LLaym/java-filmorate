package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Validator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        Validator.validateFilm(film);

        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Validator.validateFilm(film);

        return filmService.updateFilm(film);
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("{id}")
    public Film findFilmById(@PathVariable Integer id) {
        Validator.validateFilmId(id);

        return filmService.findFilmById(id);
    }

    @PutMapping("{id}/like/{userId}")
    public boolean likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        Validator.validateFilmId(id);
        Validator.validateUserId(userId);

        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public boolean dislikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        Validator.validateFilmId(id);
        Validator.validateUserId(userId);

        return filmService.dislikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(
            @RequestParam(defaultValue = "10") Integer count,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) Integer year) {

        if (genreId != null && year != null) {
            return filmService.findTopFilmsByGenreAndYear(count, genreId, year);
        } else if (genreId != null) {
            // Возвращаем список фильмов только с учетом жанра
            return filmService.findTopFilmsByGenreAndYear(count, genreId, null);
        } else if (year != null) {
            // Возвращаем список фильмов только с учетом года выпуска
            return filmService.findTopFilmsByGenreAndYear(count, null, year);
        } else {
            // Возвращаем общий список популярных фильмов
            return filmService.findTopFilms(count);
        }
    }

    @GetMapping("/common")
    public List<Film> findCommonFilms(
            @RequestParam Integer userId,
            @RequestParam Integer friendId) {
        Validator.validateUserId(userId);
        Validator.validateUserId(friendId);

        return filmService.findCommonFilms(userId, friendId);
    }

    @GetMapping("director/{directorId}")
    public List<Film> findFilmsByDirector(@PathVariable Integer directorId, @RequestParam(defaultValue = "year") String sortBy) {
        Validator.validateDirectorId(directorId);

        return filmService.findFilmsByDirector(directorId, sortBy);
    }

    @GetMapping("search")
    public List<Film> findFilmsByNameAndDirector(@RequestParam String query,
                                                 @RequestParam(required = false) List<String> by) {
        if (by == null || (by.contains("title") && by.contains("director"))) {
            return filmService.findFilmsByFilmNameAndDirectorName(query);
        } else if (by.contains("title") || (!by.contains("title") && !by.contains("director"))) {
            return filmService.findFilmsByFilmName(query);
        } else if (!by.contains("title") && by.contains("director")) {
            return filmService.findFilmsByDirectorName(query);
        }
        return filmService.findFilmsByFilmNameAndDirectorName(query);
    }
}