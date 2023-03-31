package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

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

    private void validateFilmId(Integer id) {
        if (id == null || id <= 0) {
            throw new ValidationException("параметр id не может быть меньше 0");
        }
        if (filmStorage.getFilmById(id) == null) {
            throw new FilmNotFoundException("фильма с таким id не существует");
        }
    }

    private void validateFilm(Film film) throws ValidationException {
        boolean isNewFilm = film.getId() == 0;
        if (isNewFilm) {
            if (film.getName() == null || film.getName().equals("")) {
                throw new ValidationException("название не может быть пустым");
            } else if (film.getDescription().length() > 200) {
                throw new ValidationException("максимальная длина описания — 200 символов");
            } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
                throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
            } else if (film.getDuration() <= 0) {
                throw new ValidationException("продолжительность фильма должна быть положительной");
            }
        } else {
            if (filmStorage.getAllFilms().stream().noneMatch(film1 -> film1.getId() == film.getId())) {
                throw new FilmNotFoundException("фильма с таким id не существует");
            }
        }
    }
}
