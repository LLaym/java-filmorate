package ru.yandex.practicum.filmorate.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

@Component
public class Validator {
    private static FilmStorage filmStorage;
    private static UserStorage userStorage;
    private static DirectorStorage directorStorage;
    private static MpaStorage mpaStorage;
    private static GenreStorage genreStorage;
    private static ReviewStorage reviewStorage;

    @Autowired
    public Validator(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                     @Qualifier("userDbStorage") UserStorage userStorage,
                     DirectorStorage directorStorage,
                     MpaStorage mpaStorage,
                     GenreStorage genreStorage,
                     ReviewStorage reviewStorage) {
        Validator.filmStorage = filmStorage;
        Validator.userStorage = userStorage;
        Validator.directorStorage = directorStorage;
        Validator.mpaStorage = mpaStorage;
        Validator.genreStorage = genreStorage;
        Validator.reviewStorage = reviewStorage;
    }

    public static void validateFilmId(Integer id) {
        // Исправил по подобию validateUserId - тесты не рушатся
        if (id == null) {
            throw new ValidationException("требуется корректный id параметр");
        }
        if (filmStorage.getById(id).isEmpty()) {
            throw new FilmNotFoundException("фильма с таким id не существует");
        }
    }

    public static void validateFilm(Film film) throws ValidationException {
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
            if (filmStorage.getAll().stream().noneMatch(film1 -> film1.getId() == film.getId())) {
                throw new FilmNotFoundException("фильма с таким id не существует");
            }
        }

        if (film.getMpa() != null) checkMpa(film.getMpa());
        if (film.getGenres() != null) checkGenres(film.getGenres());
        if (film.getDirectors() != null) checkDirector(film.getDirectors());
    }

    public static void validateUserId(Integer id) {
        if (id == null) {
            throw new ValidationException("требуется корректный id параметр");
        }
        if (userStorage.getById(id).isEmpty()) {
            throw new UserNotFoundException("пользователя с таким id не существует");
        }
    }

    public static void validateUser(User user) throws ValidationException {
        boolean isNewUser = user.getId() == 0;

        if (isNewUser) {
            if (user.getEmail() == null || user.getEmail().equals("") || !user.getEmail().contains("@")) {
                throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
            } else if (user.getLogin() == null || user.getLogin().equals("") || user.getLogin().contains(" ")) {
                throw new ValidationException("логин не может быть пустым и содержать пробелы");
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("дата рождения не может быть в будущем.");
            }
        } else {
            if (userStorage.getAll().stream().noneMatch(user1 -> user1.getId() == user.getId())) {
                throw new UserNotFoundException("пользователя с таким id не существует");
            }
        }
    }

    public static void validateDirectorId(Integer id) {
        if (id == null || id < 1) {
            throw new ValidationException("требуется корректный id параметр");
        }
        if (directorStorage.getById(id).isEmpty()) {
            throw new DirectorNotFoundException("режисёра с таким id не существует");
        }
    }

    public static void validateDirector(Director director) throws ValidationException {
        boolean isNewDirector = director.getId() == null;

        if (isNewDirector) {
            if (director.getName() == null || director.getName().equals("")) {
                throw new ValidationException("имя режисёра не может быть пустым");
            }
        } else {
            if (directorStorage.getAll().stream().noneMatch(director1 -> director1.getId() == director.getId())) {
                throw new DirectorNotFoundException("режисёра с таким id не существует");
            }
        }
    }

    private static void checkMpa(Mpa mpa) {
        mpaStorage.getById(mpa.getId())
                .orElseThrow(() -> new ValidationException("Mpa с id " + mpa.getId() + " не существует"));
    }

    private static void checkGenres(Set<Genre> genres) {
        for (Genre genre : genres) {
            genreStorage.getById(genre.getId())
                    .orElseThrow(() -> new ValidationException("Жанра с id " + genre.getId() + " не существует"));
        }
    }

    private static void checkDirector(List<Director> director) {
        for (Director director1 : director) {
            directorStorage.getById(director1.getId())
                    .orElseThrow(() -> new ValidationException("Режисёра с id " + director1.getId() + " не существует"));
        }
    }

    public static void validateReviewId(Integer id) {
        if (id == null) {
            throw new ValidationException("требуется корректный id параметр");
        }
        if (filmStorage.getById(id).isEmpty()) {
            throw new ReviewNotFoundException("отзыва с таким id не существует");
        }
    }

    public static void validateReview(Review review) throws ValidationException {
        boolean isNewReview = review.getReviewId() == null;

        if (isNewReview) {
            validateUserId(review.getUserId());
            validateFilmId(review.getFilmId());
            if (review.getContent() == null || review.getContent().length() > 500) {
                throw new ValidationException("максимальная длина описания — 500 символов");
            } else if (review.getIsPositive() == null) {
                throw new ValidationException("требуется корректный параметр is_positive");
            }
        } else {
            if (reviewStorage.getById(review.getReviewId()).isEmpty()) {
                throw new ReviewNotFoundException("отзыва с таким id не существует");
            }
        }
    }
}