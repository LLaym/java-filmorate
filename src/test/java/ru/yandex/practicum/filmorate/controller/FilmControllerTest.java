package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void BeforeEach() {
        filmController = new FilmController();
    }

    @Test
    void shouldAdd() {
        Film film = new Film();
        film.setName("Shining");
        film.setDescription("Horror");
        film.setReleaseDate(LocalDate.of(1980, Month.MAY, 23));
        film.setDuration(226);

        filmController.createUser(film);
        assertEquals(1, filmController.findAllUsers().size());
    }

    @Test
    void shouldSkipEmptyName() {
        Film film = new Film();
        film.setDescription("Horror");
        film.setReleaseDate(LocalDate.of(1980, Month.MAY, 23));
        film.setDuration(226);

        assertThrows(RuntimeException.class, () -> filmController.createUser(film));
        assertEquals(0, filmController.findAllUsers().size());
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

        assertThrows(RuntimeException.class, () -> filmController.createUser(film));
        assertEquals(0, filmController.findAllUsers().size());
    }

    @Test
    void shouldSkipUnsupportedDate() {
        Film film = new Film();
        film.setName("Shining");
        film.setDescription("Horror");
        film.setReleaseDate(LocalDate.of(1790, Month.MAY, 23));
        film.setDuration(226);

        assertThrows(RuntimeException.class, () -> filmController.createUser(film));
        assertEquals(0, filmController.findAllUsers().size());
    }

    @Test
    void zeroDurationFilm() {
        Film film = new Film();
        film.setName("Shining");
        film.setDescription("Horror");
        film.setReleaseDate(LocalDate.of(1980, Month.MAY, 23));
        film.setDuration(0);

        assertThrows(RuntimeException.class, () -> filmController.createUser(film));
        assertEquals(0, filmController.findAllUsers().size());
    }
}