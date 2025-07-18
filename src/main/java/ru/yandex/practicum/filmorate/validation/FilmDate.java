package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FilmDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FilmDate {
    String message() default "Invalid film date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
