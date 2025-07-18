package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface InMemoryStorageCRUD<T> {
    T createEntityInStorage(T entity);

    void deleteEntityFromStorage(Long id);

    T readEntityFromStorage(Long id);

    T updateEntityInStorage(T entity);

    List<T> readAllEntityFromStorage();
}
