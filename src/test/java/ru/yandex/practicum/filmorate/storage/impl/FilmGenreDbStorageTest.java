package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmGenreDbStorageTest {
    private final FilmGenreDbStorage filmGenreStorage;
    private final FilmDbStorage filmStorage;

    @Test
    void test() {
        Film film = Film.builder()
                .name("Shining")
                .description("Horror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226)
                .mpa(new Mpa(1, "test"))
                .build();

        filmStorage.save(film);

        filmGenreStorage.save(1, 1);
        List<FilmGenre> filmGenres = filmGenreStorage.findAllByFilmId(1);

        assertFalse(filmGenres.isEmpty());
        assertEquals(1, filmGenres.get(0).getFilmId());
        assertEquals(1, filmGenres.get(0).getGenreId());

        filmGenreStorage.deleteAllByFilmId(1);

        List<FilmGenre> filmGenres2 = filmGenreStorage.findAllByFilmId(1);

        assertTrue(filmGenres2.isEmpty());
    }
}