package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmService filmService;
    FilmController filmController;

    @BeforeEach
    void BeforeEach() {
        filmService = new FilmService(new InMemoryFilmStorage());
        filmController = new FilmController(filmService);
    }

    @Test
    void shouldAdd() {
        Film film = new Film();
        film.setName("Shining");
        film.setDescription("Horror");
        film.setReleaseDate(LocalDate.of(1980, Month.MAY, 23));
        film.setDuration(226);

        filmController.createFilm(film);
        assertEquals(1, filmController.findAllFilms().size());
    }

    @Test
    void shouldSkipEmptyName() {
        Film film = new Film();
        film.setDescription("Horror");
        film.setReleaseDate(LocalDate.of(1980, Month.MAY, 23));
        film.setDuration(226);

        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
        assertEquals(0, filmController.findAllFilms().size());
    }

    @Test
    void shouldSkipTooLongDescription() {
        Film film = new Film();
        film.setName("Shining");
        film.setDescription("HorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorror" +
                "HorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorror" +
                "HorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorror");
        film.setReleaseDate(LocalDate.of(1980, Month.MAY, 23));
        film.setDuration(226);

        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
        assertEquals(0, filmController.findAllFilms().size());
    }

    @Test
    void shouldSkipUnsupportedDate() {
        Film film = new Film();
        film.setName("Shining");
        film.setDescription("Horror");
        film.setReleaseDate(LocalDate.of(1790, Month.MAY, 23));
        film.setDuration(226);

        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
        assertEquals(0, filmController.findAllFilms().size());
    }

    @Test
    void zeroDurationFilm() {
        Film film = new Film();
        film.setName("Shining");
        film.setDescription("Horror");
        film.setReleaseDate(LocalDate.of(1980, Month.MAY, 23));
        film.setDuration(0);

        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
        assertEquals(0, filmController.findAllFilms().size());
    }
}