package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@Positive @PathVariable Long id,
                         @Positive @PathVariable Long userId) {
        filmService.addLikeToFilm(id, userId);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@Positive @PathVariable("id") Long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@Positive @RequestParam(required = false) Integer count) {
        int filmCount = count == null ? 10 : count;
        return filmService.getTopRatedFilms(filmCount);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteFilmLike(@Positive @PathVariable Long id,
                               @Positive @PathVariable Long userId) {
        filmService.removeLikeFromFilm(id, userId);
    }


}
