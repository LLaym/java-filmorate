package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.ArrayList;
import java.util.Collection;
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

        return filmStorage.saveFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Обновлён фильм: {}", film);

        return filmStorage.updateFilm(film);
    }

    public Collection<Film> findAllFilms() {
        log.info("Возвращен список всех фильмов");

        return filmStorage.getAllFilms();
    }

    public Film findFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);

        log.info("Получен фильм: {}", film);

        return film;
    }

    public Collection<Like> likeFilm(Integer filmId, Integer userId) {
        List<Like> likes = (List<Like>) likeStorage.saveLike(filmId, userId);

        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);

        return likes;
    }

    public Collection<Like> dislikeFilm(Integer filmId, Integer userId) {
        Collection<Like> likes = likeStorage.removeLike(filmId, userId);

        log.info("Пользователь с id {} убрал лайк с фильма с id {}", userId, filmId);

        return likes;
    }

    public Collection<Film> findTopFilms(Integer count) {
        List<Film> films = new ArrayList<>();

        Collection<Integer> topFilms = likeStorage.getTopFilms(count);

        topFilms.forEach(integer -> films.add(filmStorage.getFilmById(integer)));

        log.info("Возвращен топ фильмов: ", films);

        return films;
    }
}