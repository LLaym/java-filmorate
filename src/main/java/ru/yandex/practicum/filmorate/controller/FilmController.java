package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private FilmService filmService;

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
    public Film likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        Validator.validateFilmId(id);
        Validator.validateUserId(userId);
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film dislikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        Validator.validateFilmId(id);
        Validator.validateUserId(userId);
        return filmService.dislikeFilm(id, userId);
    }

    @GetMapping("popular")
    public Collection<Film> findTopFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.findTopFilms(count);
    }
}