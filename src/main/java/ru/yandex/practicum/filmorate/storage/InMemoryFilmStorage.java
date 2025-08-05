package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utility.Utility;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements InMemoryStorageCRUD<Film> {
    private final Map<Long, Film> filmStorage;

    public InMemoryFilmStorage() {
        this.filmStorage = new HashMap<>();
    }

    @Override
    public Film createEntityInStorage(Film entity) {
        Long id = Utility.getIdForEntity();

        Film toCreate = Film.builder()
                .id(id)
                .name(entity.getName())
                .description(entity.getDescription())
                .releaseDate(entity.getReleaseDate())
                .duration(entity.getDuration())
                .likes(new HashSet<>())
                .build();
        filmStorage.put(id, toCreate);
        log.info("Film created: {}", toCreate);
        return toCreate;
    }

    @Override
    public void deleteEntityFromStorage(Long id) {
        if (filmStorage.containsKey(id)) {
            filmStorage.remove(id);
            log.info("Film deleted: {}", id);
        } else {
            throw new NotFoundException(id);
        }
    }

    @Override
    public Film readEntityFromStorage(Long id) {
        if (filmStorage.containsKey(id)) {
            return filmStorage.get(id);
        } else {
            throw new NotFoundException(id);
        }
    }

    @Override
    public Film updateEntityInStorage(Film entity) {
        if (filmStorage.containsKey(entity.getId())) {
            Film toUpdate = Film.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .description(entity.getDescription())
                    .releaseDate(entity.getReleaseDate())
                    .duration(entity.getDuration())
                    .likes(entity.getLikes())
                    .build();
            filmStorage.put(entity.getId(), toUpdate);
            log.info("Film updated: {}", toUpdate);
            return toUpdate;
        } else {
            throw new NotFoundException(entity.getId());
        }
    }

    @Override
    public List<Film> readAllEntityFromStorage() {
        return filmStorage.values().stream().collect(Collectors.toList());
    }
}
