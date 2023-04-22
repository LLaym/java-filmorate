package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmService filmService;
    FilmController filmController;

    @BeforeEach
    void initEach() {
        filmService = new FilmService(new InMemoryFilmStorage());
        filmController = new FilmController(filmService);
    }

    @Test
    void shouldAdd() {
        Film film = Film.builder().name("Shining").description("Horror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226).build();
        filmController.createFilm(film);

        assertEquals(1, filmController.findAllFilms().size());
    }

    @Test
    void shouldSkipEmptyName() {
        Film film = Film.builder().description("Horror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226).build();

        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
        assertEquals(0, filmController.findAllFilms().size());
    }

    @Test
    void shouldSkipTooLongDescription() {
        Film film = Film.builder().name("Shining").description("HorrorHorrorHorrorHorrorHorror" +
                        "HorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorror" +
                        "HorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorror" +
                        "HorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorrorHorror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226).build();

        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
        assertEquals(0, filmController.findAllFilms().size());
    }

    @Test
    void shouldSkipUnsupportedDate() {
        Film film = Film.builder().name("Shining").description("Horror")
                .releaseDate(LocalDate.of(1790, Month.MAY, 23))
                .duration(226).build();

        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
        assertEquals(0, filmController.findAllFilms().size());
    }

    @Test
    void zeroDurationFilm() {
        Film film = Film.builder().name("Shining").description("Horror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(0).build();

        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
        assertEquals(0, filmController.findAllFilms().size());
    }
}