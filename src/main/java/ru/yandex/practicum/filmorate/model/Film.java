package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.FilmDate;
import ru.yandex.practicum.filmorate.validation.FilmDuration;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Film {
    private Long id;

    @NotBlank(message = "Specify name field for the film")
    private String name;

    @Size(message = "Description should be less then 200 chars length", max = 200)
    private String description;

    @FilmDate(message = "Film date should be after 28.12.1985")
    private LocalDate releaseDate;

    @FilmDuration
    private Duration duration;

    private Set<Long> likes;
}
