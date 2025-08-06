package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryStorageCRUD;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final InMemoryStorageCRUD<User> userStorage;

    public User createUser(User user) {
        return userStorage.createEntityInStorage(user);
    }

    public User updateUser(User user) {
        return userStorage.updateEntityInStorage(user);
    }

    public List<User> getUsers() {
        return userStorage.readAllEntityFromStorage();
    }

    public User getUser(Long id) {
        return userStorage.readEntityFromStorage(id);
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.readEntityFromStorage(userId).getFriends().add(friendId);
        userStorage.readEntityFromStorage(friendId).getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
       userStorage.readEntityFromStorage(userId).getFriends().remove(friendId);
       userStorage.readEntityFromStorage(friendId).getFriends().remove(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = userStorage.readEntityFromStorage(userId);
        User otherUser = userStorage.readEntityFromStorage(otherUserId);

        Set<Long> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(otherUser.getFriends());

        return intersection.stream()
                .map(id -> userStorage.readEntityFromStorage(id))
                .collect(Collectors.toList());
    }

    public List<User> getUserFriends(Long userId) {
        User user = userStorage.readEntityFromStorage(userId);
        return user.getFriends().stream()
                .map(id -> userStorage.readEntityFromStorage(id))
                .collect(Collectors.toList());
    }
}
