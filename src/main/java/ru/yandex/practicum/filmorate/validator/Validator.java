package ru.yandex.practicum.filmorate.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;

@Component
public class Validator {
    private static FilmStorage filmStorage;
    private static UserStorage userStorage;

    @Autowired
    public Validator(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                     @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public static void validateFilmId(Integer id) {
        if (id == null || id <= 0) {
            throw new ValidationException("параметр id не может быть меньше 0");
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
}