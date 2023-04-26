package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
    }

    public Film createFilm(Film film) {
        log.info("Добавлен фильм: {}", film);
        return filmStorage.save(film);
    }

    public Film updateFilm(Film film) {
        log.info("Обновлён фильм: {}", film);
        return filmStorage.update(film);
    }

    public List<Film> findAllFilms() {
        log.info("Возвращен список всех фильмов");
        return filmStorage.getAll();
    }

    public Film findFilmById(Integer id) {
        Film film = filmStorage.getById(id);

        log.info("Получен фильм: {}", film);
        return film;
    }

    public void likeFilm(Integer filmId, Integer userId) {
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
        likeStorage.save(filmId, userId);
    }

    public void dislikeFilm(Integer filmId, Integer userId) {
        log.info("Пользователь с id {} убрал лайк с фильма с id {}", userId, filmId);
        likeStorage.delete(filmId, userId);
    }

//    public List<Film> findTopFilms(Integer count) {
//        List<Film> topFilms = new ArrayList<>();
//
//        List<Integer> films = likeStorage.getTopFilmsId(count);
//
//        films.forEach(integer -> topFilms.add(filmStorage.getById(integer)));
//
//        log.info("Возвращен топ фильмов: ", topFilms);
//        return topFilms;
//    }
}