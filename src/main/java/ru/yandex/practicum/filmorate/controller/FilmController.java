package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

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

    @GetMapping("popular")
    public List<Film> findTopFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.findTopFilms(count);
    }
}