package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.FilmDirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.model.EventOperation.ADD;
import static ru.yandex.practicum.filmorate.model.EventOperation.REMOVE;
import static ru.yandex.practicum.filmorate.model.EventType.LIKE;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final FilmDirectorStorage filmDirectorStorage;
    private final EventStorage eventStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       LikeStorage likeStorage,
                       FilmDirectorStorage filmDirectorStorage,
                       EventStorage eventStorage) {
        this.filmStorage = filmStorage;
        this.likeStorage = likeStorage;
        this.filmDirectorStorage = filmDirectorStorage;
        this.eventStorage = eventStorage;
    }

    public Film createFilm(Film film) {
        int generatedId = filmStorage.save(film);
        Film createdFilm = filmStorage.getById(generatedId).orElse(null);

        log.info("Добавлен фильм: {}", createdFilm);
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        filmStorage.update(film);
        Film updatedFilm = filmStorage.getById(film.getId()).orElse(null);

        log.info("Обновлён фильм: {}", updatedFilm);
        return updatedFilm;
    }

    public List<Film> findAllFilms() {
        log.info("Возвращен список всех фильмов");
        return filmStorage.getAll();
    }

    public Film findFilmById(Integer filmId) {
        Film film = filmStorage.getById(filmId)
                .orElseThrow(() -> new FilmNotFoundException("Фильм с id " + filmId + " не найден"));

        log.info("Получен фильм: {}", film);
        return film;
    }

    public boolean likeFilm(Integer filmId, Integer userId) {
        if (likeStorage.save(filmId, userId)) {
            log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);

            Event event = Event.builder()
                    .userId(userId)
                    .entityId(filmId)
                    .eventType(LIKE)
                    .operation(ADD)
                    .build();
            eventStorage.save(event);

            return true;
        }
        return false;
    }

    public boolean dislikeFilm(Integer filmId, Integer userId) {
        if (likeStorage.delete(filmId, userId)) {
            log.info("Пользователь с id {} убрал лайк с фильма с id {}", userId, filmId);

            Event event = Event.builder()
                    .userId(userId)
                    .entityId(filmId)
                    .eventType(LIKE)
                    .operation(REMOVE)
                    .build();
            eventStorage.save(event);

            return true;
        }
        return false;
    }

    public List<Film> findTopFilms(Integer count) {
        List<Film> topFilms = likeStorage.getPopularFilmsIds(count)
                .stream()
                .map(filmStorage::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Возвращен топ фильмов: {} ", topFilms);
        return topFilms;
    }

    public List<Film> getRecommendations(Integer userId) {
        List<Film> recommendedFilms = likeStorage.getRecommendFilmsIds(userId)
                .stream()
                .map(filmStorage::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Возвращены рекомендации для пользователя с id {}: {} ", userId, recommendedFilms);
        return recommendedFilms;
    }

    public List<Film> findCommonFilms(Integer userId, Integer friendId) {
        List<Film> films = likeStorage.getCommonFilmsIds(userId, friendId)
                .stream()
                .map(filmStorage::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Возвращены общие фильмы для пользователей с id {} и {}: {} ", userId, friendId, films);
        return films;
    }

    public List<Film> findTopFilmsByGenreAndYear(Integer count, Integer genreId, Integer year) {
        List<Film> topFilms = likeStorage.getPopularFilmsIds(count)
                .stream()
                .map(filmStorage::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        List<Film> topFilmsFiltered = topFilms.stream()
                .filter(f -> (year == null || f.getReleaseDate().getYear() == year)
                        && (genreId == null || f.getGenres().stream().anyMatch(genre -> genre.getId().equals(genreId))))
                .collect(Collectors.toList());

        log.info("Возвращен топ фильмов: {}", topFilmsFiltered);
        return topFilmsFiltered;
    }

    public List<Film> findFilmsByDirector(Integer directorId, String sortBy) {
        List<Film> directorFilms = filmDirectorStorage.getAllByDirector(directorId)
                .stream()
                .map(FilmDirector::getFilmId)
                .map(filmStorage::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (sortBy.equals("year")) {
            directorFilms.sort(Comparator.comparing(Film::getReleaseDate));
        } else if (sortBy.equals("likes")) {
            directorFilms.sort((film1, film2) ->
                    likeStorage.getAllByFilmId(film1.getId()).size() - likeStorage.getAllByFilmId(film2.getId()).size());
        } else {
            throw new RuntimeException("Укажите параметр сортировки year/likes");
        }

        log.info("Возвращен список фильмов режиссёра: {} ", directorFilms);
        return directorFilms;
    }
}