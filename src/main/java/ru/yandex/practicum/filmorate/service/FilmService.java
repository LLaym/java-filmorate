package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmDirector;
import ru.yandex.practicum.filmorate.storage.*;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.model.EventOperation.ADD;
import static ru.yandex.practicum.filmorate.model.EventOperation.REMOVE;
import static ru.yandex.practicum.filmorate.model.EventType.LIKE;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final FilmDirectorStorage filmDirectorStorage;
    private final EventStorage eventStorage;
    private final DirectorStorage directorStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       UserStorage userStorage,
                       LikeStorage likeStorage,
                       FilmDirectorStorage filmDirectorStorage,
                       EventStorage eventStorage,
                       DirectorStorage directorStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.filmDirectorStorage = filmDirectorStorage;
        this.eventStorage = eventStorage;
        this.directorStorage = directorStorage;
    }

    public Film createFilm(Film film) {
        int generatedId = filmStorage.save(film);
        Film createdFilm = filmStorage.findById(generatedId).orElse(null);

        log.info("Добавлен фильм: {}", createdFilm);
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        Integer filmId = film.getId();

        if (filmStorage.notExists(filmId)) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }

        filmStorage.update(film);
        Film updatedFilm = filmStorage.findById(film.getId()).orElse(null);

        log.info("Обновлён фильм: {}", updatedFilm);
        return updatedFilm;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.findAll();

        log.info("Возвращен список всех фильмов");
        return films;
    }

    public Film getFilmById(Integer filmId) {
        Film film = filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id " + filmId + " не найден"));

        log.info("Получен фильм: {}", film);
        return film;
    }

    public void deleteFilmById(Integer filmId) {
        if (filmStorage.notExists(filmId)) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }

        filmStorage.deleteById(filmId);
        log.info("Фильм с id {} удален: ", filmId);
    }

    public boolean likeFilm(Integer filmId, Integer userId) {
        try {
            if (filmStorage.notExists(filmId)) {
                throw new NotFoundException("Фильм с id " + filmId + " не найден");
            } else if (userStorage.notExists(userId)) {
                throw new NotFoundException("Пользователь с id " + userId + " не найден");
            }

            likeStorage.save(filmId, userId);
            log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);

            return true;
        } catch (DataAccessException e) {
            return false;
        } finally {
            Event event = Event.builder()
                    .userId(userId)
                    .entityId(filmId)
                    .eventType(LIKE)
                    .operation(ADD)
                    .build();

            eventStorage.save(event);
        }
    }

    public void dislikeFilm(@NotNull Integer filmId, @NotNull Integer userId) {
        if (filmStorage.notExists(filmId)) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        } else if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        likeStorage.delete(filmId, userId);
        log.info("Пользователь с id {} убрал лайк с фильма с id {}", userId, filmId);

        Event event = Event.builder()
                .userId(userId)
                .entityId(filmId)
                .eventType(LIKE)
                .operation(REMOVE)
                .build();
        eventStorage.save(event);
    }

    public List<Film> getTopFilms(Integer count) {
        List<Film> topFilms = likeStorage.findPopularFilmsIds(count)
                .stream()
                .map(filmStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Возвращен топ фильмов: {} ", topFilms);
        return topFilms;
    }

    public List<Film> getRecommendations(Integer userId) {
        if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        List<Film> recommendedFilms = likeStorage.findRecommendFilmsIds(userId)
                .stream()
                .map(filmStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Возвращены рекомендации для пользователя с id {}: {} ", userId, recommendedFilms);
        return recommendedFilms;
    }

    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else if (userStorage.notExists(friendId)) {
            throw new NotFoundException("Пользователь с id " + friendId + " не найден");
        }

        List<Film> films = likeStorage.findCommonFilmsIds(userId, friendId)
                .stream()
                .map(filmStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        log.info("Возвращены общие фильмы для пользователей с id {} и {}: {} ", userId, friendId, films);
        return films;
    }

    public List<Film> getTopFilmsByGenreAndYear(Integer count, Integer genreId, Integer year) {
        List<Film> topFilms = likeStorage.findPopularFilmsIds(count)
                .stream()
                .map(filmStorage::findById)
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

    public List<Film> getFilmsByDirector(Integer directorId, String sortBy) {
        if (directorStorage.notExists(directorId)) {
            throw new NotFoundException("Режиссёр с id " + directorId + " не найден");
        }

        List<Film> directorFilms = filmDirectorStorage.findAllByDirector(directorId)
                .stream()
                .map(FilmDirector::getFilmId)
                .map(filmStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (sortBy.equals("year")) {
            directorFilms.sort(Comparator.comparing(Film::getReleaseDate));
        } else if (sortBy.equals("likes")) {
            directorFilms.sort((film1, film2) ->
                    likeStorage.findAllByFilmId(film1.getId()).size() - likeStorage.findAllByFilmId(film2.getId()).size());
        } else {
            throw new RuntimeException("Укажите параметр сортировки year/likes");
        }

        log.info("Возвращен список фильмов режиссёра: {} ", directorFilms);
        return directorFilms;
    }

    public List<Film> getFilmsByFilmName(String query) {
        List<Film> films = filmStorage.findAllByNameSubstring(query);

        films.sort((film1, film2) ->
                likeStorage.findAllByFilmId(film2.getId()).size() - likeStorage.findAllByFilmId(film1.getId()).size());

        log.info("Возвращен список найденных фильмов: {} ", films);
        return films;
    }

    public List<Film> getFilmsByDirectorName(String query) {
        List<Film> films = new ArrayList<>();
        List<Director> directors = directorStorage.findAllByNameSubstring(query);

        for (Director director : directors) {
            films.addAll(filmDirectorStorage.findAllByDirector(director.getId())
                    .stream()
                    .map(FilmDirector::getFilmId)
                    .map(filmStorage::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));
        }

        films.sort((film1, film2) ->
                likeStorage.findAllByFilmId(film2.getId()).size() - likeStorage.findAllByFilmId(film1.getId()).size());

        log.info("Возвращен список найденных фильмов: {} ", films);
        return films;
    }

    public List<Film> getFilmsByFilmNameAndDirectorName(String query) {
        Set<Film> films = new TreeSet<>((film1, film2) ->
                likeStorage.findAllByFilmId(film2.getId()).size() - likeStorage.findAllByFilmId(film1.getId()).size());
        films.addAll(filmStorage.findAllByNameSubstring(query));

        List<Director> directors = directorStorage.findAllByNameSubstring(query);

        for (Director director : directors) {
            films.addAll(filmDirectorStorage.findAllByDirector(director.getId())
                    .stream()
                    .map(FilmDirector::getFilmId)
                    .map(filmStorage::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList()));
        }

        log.info("Возвращен список найденных фильмов: {} ", films);
        return new ArrayList<>(films);
    }
}