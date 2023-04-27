package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    @Test
    void testSave() {
        Film film = Film.builder()
                .name("Shining")
                .description("Horror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226)
                .mpa(new Mpa(1, "test"))
                .build();

        int generatedId = filmStorage.save(film);

        assertEquals(1, generatedId);
    }

    @Test
    void testUpdate() {
        Film updatedFilm = Film.builder()
                .id(1)
                .name("Updated Shining")
                .description("Updated Horror")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226)
                .mpa(new Mpa(1, "test"))
                .build();

        boolean isUpdated = filmStorage.update(updatedFilm);

        assertTrue(isUpdated);
    }

    @Test
    void testGetById() {
        Optional<Film> filmOptional = filmStorage.getById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void testGetAll() {
        assertEquals(1, filmStorage.getAll().size());
    }
}