package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryStorageCRUD;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final InMemoryStorageCRUD<Film> filmStorage = new InMemoryFilmStorage();

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmStorage.createEntityInStorage(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateEntityInStorage(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmStorage.readAllEntityFromStorage();
    }
}
