package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable @NotNull Integer id) {
        return filmService.getFilmById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable @NotNull Integer id) {
        filmService.deleteFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean likeFilm(@PathVariable @NotNull Integer id, @PathVariable @NotNull Integer userId) {
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dislikeFilm(@PathVariable @NotNull Integer id, @PathVariable @NotNull Integer userId) {
        filmService.dislikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(
            @RequestParam(defaultValue = "10") Integer count,
            @RequestParam(required = false) Integer genreId,
            @RequestParam(required = false) Integer year) {
        if (genreId == null && year == null) {
            return filmService.getTopFilms(count);
        } else {
            return filmService.getTopFilmsByGenreAndYear(count, genreId, year);
        }
    }

    @GetMapping("/common")
    public List<Film> findCommonFilms(
            @RequestParam @NotNull Integer userId,
            @RequestParam @NotNull Integer friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> findFilmsByDirector(@PathVariable @NotNull Integer directorId,
                                          @RequestParam(defaultValue = "year") String sortBy) {
        return filmService.getFilmsByDirector(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> findFilmsByNameAndDirector(@RequestParam String query,
                                                 @RequestParam(required = false) List<String> by) {
        if (by == null || (by.contains("title") && by.contains("director"))) {
            return filmService.getFilmsByFilmNameAndDirectorName(query);
        } else if (by.contains("title") || (!by.contains("title") && !by.contains("director"))) {
            return filmService.getFilmsByFilmName(query);
        } else if (!by.contains("title") && by.contains("director")) {
            return filmService.getFilmsByDirectorName(query);
        }
        return filmService.getFilmsByFilmNameAndDirectorName(query);
    }
}