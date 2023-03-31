package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.service.Validator.validateFilm;
import static ru.yandex.practicum.filmorate.service.Validator.validateFilmId;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    private static int nextId;
    private FilmStorage filmStorage;

    public Film createFilm(Film film) {
        validateFilm(film);
        Film filmWithId = film.toBuilder().id(++nextId).build();
        log.info("Добавлен фильм: {}", filmWithId);
        return filmStorage.createFilm(filmWithId);
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        log.info("Обновлён фильм: {}", film);
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> findAllFilms() {
        log.info("Возвращен список всех фильмов");
        return filmStorage.getAllFilms();
    }

    public Film findFilmById(Integer id) {
        validateFilmId(id);
        Film film = filmStorage.getFilmById(id);
        log.info("Получен фильм: {}", film);
        return film;
    }

//    public Film likeFilm(Integer id, Integer userId) {
//        validateFilmId(id);
//    }
}
