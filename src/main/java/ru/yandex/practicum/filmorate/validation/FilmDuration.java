package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FilmDurationValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FilmDuration {
    String message() default "Film duration should be positive";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}