package ru.yandex.practicum.filmorate.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@Slf4j
@RestControllerAdvice("ru.yandex.practicum.filmorate")
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationExceptionHandler(final Throwable e) {
        String error = "ошибка валидации";
        log.warn("произошла ошибка: {}. описание: {}", error, e.getMessage());
        return new ErrorResponse(error, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userNotFoundException(final UserNotFoundException e) {
        String error = "пользователь не найден";
        log.warn("произошла ошибка: {}. описание: {}", error, e.getMessage());
        return new ErrorResponse(error, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse filmNotFoundException(final FilmNotFoundException e) {
        String error = "фильм не найден";
        log.warn("произошла ошибка: {}. описание: {}", error, e.getMessage());
        return new ErrorResponse(error, e.getMessage());
    }
}
