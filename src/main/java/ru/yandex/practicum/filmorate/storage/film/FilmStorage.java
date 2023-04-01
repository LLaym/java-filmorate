package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;

public interface FilmStorage {
    Film createFilm(@Valid @RequestBody Film film);

    Film updateFilm(@Valid @RequestBody Film film);

    Collection<Film> getAllFilms();

    Film getFilmById(Integer id);
}