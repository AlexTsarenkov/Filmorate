package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryStorageCRUD;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmService {
    private final InMemoryStorageCRUD<Film> filmStorage;
    private final UserService userService;

    public Film createFilm(Film film) {
        return filmStorage.createEntityInStorage(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateEntityInStorage(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.readAllEntityFromStorage();
    }

    public List<Film> getTopRatedFilms(int count) {
        Comparator<Film> comparator = Comparator.comparing(film -> film.getLikes().size(),
                Comparator.reverseOrder());

        return filmStorage.readAllEntityFromStorage().stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.readEntityFromStorage(filmId);
    }

    public void addLikeToFilm(Long filmId, Long userId) {
        Film film = filmStorage.readEntityFromStorage(filmId);
        User user = userService.getUser(userId);

        Set<Long> likes = film.getLikes();
        likes.add(user.getId());
        film.setLikes(likes);

        filmStorage.updateEntityInStorage(film);
    }

    public void removeLikeFromFilm(Long filmId, Long userId) {
        Film film = filmStorage.readEntityFromStorage(filmId);
        User user = userService.getUser(userId);

        Set<Long> likes = film.getLikes();

        likes.remove(userId);
        film.setLikes(likes);

        filmStorage.updateEntityInStorage(film);
    }
}
