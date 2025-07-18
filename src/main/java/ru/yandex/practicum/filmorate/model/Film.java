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

@Data
@Builder
@AllArgsConstructor
public class Film {
    public Long id;
    @NotBlank(message = "Specify name field for the film")
    public String name;
    @Size(message = "Description sould be less then 200 chars length", max = 200)
    public String description;
    @FilmDate(message = "Film date should be after 28.12.1985")
    public LocalDate releaseDate;
    @FilmDuration
    public Duration duration;
}
