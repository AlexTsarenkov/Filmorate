package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryStorageCRUD;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final InMemoryStorageCRUD<User> userStorage = new InMemoryUserStorage();

    @PostMapping
    public User createUser(@Validated @RequestBody User user) {
        return userStorage.createEntityInStorage(user);
    }

    @PutMapping
    public User updateUser(@Validated @RequestBody User user) {
        return userStorage.updateEntityInStorage(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userStorage.readAllEntityFromStorage();
    }
}
