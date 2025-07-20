package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FilmDateValidator implements ConstraintValidator<FilmDate, LocalDate> {
    @Override
    public void initialize(FilmDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate filmStartDate = LocalDate.of(1895, 12, 28);
        return localDate != null && (localDate.isAfter(filmStartDate) || localDate.isEqual(filmStartDate));
    }
}
