package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class FilmDurationValidator implements ConstraintValidator<FilmDuration, Duration> {
    @Override
    public void initialize(FilmDuration constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext constraintValidatorContext) {
        return duration != null && duration.toSeconds() > 0;
    }
}
