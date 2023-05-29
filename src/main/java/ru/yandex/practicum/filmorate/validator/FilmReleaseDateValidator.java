package ru.yandex.practicum.filmorate.validator;


import ru.yandex.practicum.filmorate.annotation.FilmReleaseDateConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmReleaseDateValidator implements
        ConstraintValidator<FilmReleaseDateConstraint, LocalDate> {
    @Override
    public void initialize(FilmReleaseDateConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return !localDate.isBefore(LocalDate.of(1895, 12, 28));
    }
}
