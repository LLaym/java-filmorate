package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    @Test
    void testSave() {
        Film expectedFilm = Film.builder()
                .name("Shining")
                .description("Horror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226)
                .mpa(new Mpa(1, "test"))
                .build();

        filmStorage.save(expectedFilm);

        Film film = filmStorage.getById(1).get();

        assertEquals(film.getId(), 1);
        assertEquals(film.getName(), expectedFilm.getName());
        assertEquals(film.getDescription(), expectedFilm.getDescription());
        assertEquals(film.getReleaseDate(), expectedFilm.getReleaseDate());
        assertEquals(film.getDuration(), expectedFilm.getDuration());
        assertEquals(film.getMpa().getId(), expectedFilm.getMpa().getId());
    }

    @Test
    void testUpdate() {
        Film oldFilm = Film.builder()
                .name("Shining")
                .description("Horror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226)
                .mpa(new Mpa(1, "test"))
                .build();

        filmStorage.save(oldFilm);

        Film expectedFilm = Film.builder()
                .id(1)
                .name("Updated Shining")
                .description("Updated Horror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226)
                .mpa(new Mpa(1, "test"))
                .build();

        filmStorage.update(expectedFilm);

        Film updatedFilm = filmStorage.getById(1).get();

        assertEquals(updatedFilm.getId(), expectedFilm.getId());
        assertEquals(updatedFilm.getName(), expectedFilm.getName());
        assertEquals(updatedFilm.getDescription(), expectedFilm.getDescription());
        assertEquals(updatedFilm.getReleaseDate(), expectedFilm.getReleaseDate());
        assertEquals(updatedFilm.getDuration(), expectedFilm.getDuration());
        assertEquals(updatedFilm.getMpa().getId(), expectedFilm.getMpa().getId());
    }

    @Test
    void testGetById() {
        Film expectedFilm = Film.builder()
                .name("Shining")
                .description("Horror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226)
                .mpa(new Mpa(1, "test"))
                .build();

        filmStorage.save(expectedFilm);

        Film film = filmStorage.getById(1).get();

        assertEquals(film.getId(), 1);
        assertEquals(film.getName(), expectedFilm.getName());
        assertEquals(film.getDescription(), expectedFilm.getDescription());
        assertEquals(film.getReleaseDate(), expectedFilm.getReleaseDate());
        assertEquals(film.getDuration(), expectedFilm.getDuration());
        assertEquals(film.getMpa().getId(), expectedFilm.getMpa().getId());
    }

    @Test
    void testGetAll() {
        Film expectedFilm = Film.builder()
                .name("Shining")
                .description("Horror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226)
                .mpa(new Mpa(1, "test"))
                .build();

        filmStorage.save(expectedFilm);

        List<Film> films = filmStorage.getAll();

        assertEquals(1, films.size());
        assertEquals(films.get(0).getId(), 1);
        assertEquals(films.get(0).getName(), expectedFilm.getName());
        assertEquals(films.get(0).getDescription(), expectedFilm.getDescription());
        assertEquals(films.get(0).getReleaseDate(), expectedFilm.getReleaseDate());
        assertEquals(films.get(0).getDuration(), expectedFilm.getDuration());
        assertEquals(films.get(0).getMpa().getId(), expectedFilm.getMpa().getId());
    }
}