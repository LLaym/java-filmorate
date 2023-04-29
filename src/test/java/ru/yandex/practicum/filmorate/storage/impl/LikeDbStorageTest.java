package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikeDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final LikeDbStorage likeStorage;

    @Test
    void test() {
        Film film1 = Film.builder()
                .name("Film1")
                .description("Description")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226)
                .mpa(new Mpa(1, "test"))
                .build();

        Film film2 = Film.builder()
                .name("Film2")
                .description("Description")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226)
                .mpa(new Mpa(1, "test"))
                .build();

        Film film3 = Film.builder()
                .name("Film3")
                .description("Description")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226)
                .mpa(new Mpa(1, "test"))
                .build();

        Film film4 = Film.builder()
                .name("Film4")
                .description("Description")
                .releaseDate(LocalDate.of(1980, Month.MAY, 23))
                .duration(226)
                .mpa(new Mpa(1, "test"))
                .build();

        filmStorage.save(film1);
        filmStorage.save(film2);
        filmStorage.save(film3);
        filmStorage.save(film4);

        User user1 = User.builder()
                .name("User1")
                .email("mail@yandex.ru")
                .login("User1")
                .birthday(LocalDate.of(1990, Month.OCTOBER, 25))
                .build();

        User user2 = User.builder()
                .name("User2")
                .email("mail@yandex.ru")
                .login("User2")
                .birthday(LocalDate.of(1990, Month.OCTOBER, 25))
                .build();

        User user3 = User.builder()
                .name("User3")
                .email("mail@yandex.ru")
                .login("User3")
                .birthday(LocalDate.of(1990, Month.OCTOBER, 25))
                .build();

        userStorage.save(user1);
        userStorage.save(user2);
        userStorage.save(user3);

        likeStorage.save(1, 1);
        likeStorage.save(1, 2);
        likeStorage.save(1, 3);
        likeStorage.save(2, 1);
        likeStorage.save(2, 2);
        likeStorage.save(3, 1);

        List<Integer> popularFilms = likeStorage.getPopularFilmsIds(5);

        assertFalse(popularFilms.isEmpty());
        assertEquals(4, popularFilms.size());
        assertEquals(1, (int) popularFilms.get(0));
        assertEquals(2, (int) popularFilms.get(1));
        assertEquals(3, (int) popularFilms.get(2));
        assertEquals(4, (int) popularFilms.get(3));

        likeStorage.delete(1, 1);
        likeStorage.delete(1, 2);
        likeStorage.delete(1, 3);

        List<Integer> popularFilms2 = likeStorage.getPopularFilmsIds(5);

        assertFalse(popularFilms2.isEmpty());
        assertEquals(4, popularFilms2.size());
        assertEquals(2, (int) popularFilms2.get(0));
        assertEquals(3, (int) popularFilms2.get(1));
        assertEquals(1, (int) popularFilms2.get(2));
        assertEquals(4, (int) popularFilms2.get(3));
    }
}