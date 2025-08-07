package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.Utility;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements InMemoryStorageCRUD<User> {
    private final Map<Long, User> userStorage;

    public InMemoryUserStorage() {
        this.userStorage = new HashMap<>();
    }

    @Override
    public User createEntityInStorage(User entity) {

        Long id = Utility.getIdForEntity();

        User toCreate = User.builder()
                .id(id)
                .login(entity.getLogin())
                .email(entity.getEmail())
                .birthday(entity.getBirthday())
                .name(entity.getName() == null || entity.getName().isBlank() ? entity.getLogin() : entity.getName())
                .friends(new HashSet<>())
                .build();
        userStorage.put(id, toCreate);
        log.info("User created: {}", toCreate);
        return toCreate;
    }

    @Override
    public void deleteEntityFromStorage(Long id) {
        if (userStorage.containsKey(id)) {
            userStorage.remove(id);
            log.info("User deleted: {}", id);
        } else {
            throw new NotFoundException(id);
        }
    }

    @Override
    public User readEntityFromStorage(Long id) {
        if (userStorage.containsKey(id)) {
            return userStorage.get(id);
        } else {
            throw new NotFoundException(id);
        }
    }

    @Override
    public User updateEntityInStorage(User entity) {
        if (userStorage.containsKey(entity.getId())) {
            User toUpdate = User.builder()
                    .id(entity.getId())
                    .login(entity.getLogin())
                    .email(entity.getEmail())
                    .birthday(entity.getBirthday())
                    .name(entity.getName() == null || entity.getName().isBlank() ? entity.getLogin() : entity.getName())
                    .friends(entity.getFriends())
                    .build();
            userStorage.put(entity.getId(), toUpdate);
            log.info("User updated: {}", toUpdate);
            return toUpdate;
        } else {
            throw new NotFoundException(entity.getId());
        }
    }

    @Override
    public List<User> readAllEntityFromStorage() {
        return userStorage.values().stream().collect(Collectors.toList());
    }
}
