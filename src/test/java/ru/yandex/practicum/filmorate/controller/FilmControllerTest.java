package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {
    private final FilmController filmController;

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
        assertEquals(1, filmController.findAllFilms().size());
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
        assertEquals(1, filmController.findAllFilms().size());
    }

    @Test
    void shouldSkipUnsupportedDate() {
        Film film = Film.builder().name("Shining").description("Horror")
                .releaseDate(LocalDate.of(1790, Month.MAY, 23))
                .duration(226).build();

        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
        assertEquals(1, filmController.findAllFilms().size());
    }

    @Test
    void shouldSkipZeroDurationFilm() {
        Film film = Film.builder().name("Shining").description("Horror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(0).build();

        assertThrows(RuntimeException.class, () -> filmController.createFilm(film));
        assertEquals(1, filmController.findAllFilms().size());
    }
}