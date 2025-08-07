package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryStorageCRUD;


import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemoryStorageCRUD<Film> filmStorage;

    @Autowired
    private InMemoryStorageCRUD<User> userStorage;

    @Test
    @DisplayName("Film creation - ok scenario")
    void filmCreationOk() throws Exception {
        Film film = Film.builder()
                .name("Test film")
                .description("Test film description")
                .releaseDate(LocalDate.of(1990, 9, 27))
                .duration(Duration.ofMinutes(120))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(film);

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Film creation - empty film name")
    void filmCreationEmptyName() throws Exception {
        Film film = Film.builder()
                .name("")
                .description("Test film description")
                .releaseDate(LocalDate.of(1990, 9, 27))
                .duration(Duration.ofMinutes(120))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(film);

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Film creation - date not valid")
    void filmCreationWrongDate() throws Exception {
        Film film = Film.builder()
                .name("Test")
                .description("Test film description")
                .releaseDate(LocalDate.of(1895, 11, 27))
                .duration(Duration.ofMinutes(120))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(film);

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Film creation - duration not valid")
    void filmCreationWrongDuration() throws Exception {
        Film film = Film.builder()
                .name("Test")
                .description("Test film description")
                .releaseDate(LocalDate.of(1896, 11, 27))
                .duration(Duration.ofMinutes(-1))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(film);

        this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Film update - ok scenario")
    void filmUpdateIsOk() throws Exception {
        Film film = Film.builder()
                .name("Test film")
                .description("Test film description")
                .releaseDate(LocalDate.of(1990, 9, 27))
                .duration(Duration.ofMinutes(120))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(film);

        MvcResult result = this.mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Film filmToUpdate = mapper.readValue(content, Film.class);
        filmToUpdate.setName("Updated film");
        jsonString = mapper.writeValueAsString(filmToUpdate);

        result = this.mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        content = result.getResponse().getContentAsString();
        Film updatedFilm = mapper.readValue(content, Film.class);
        Assertions.assertEquals("Updated film", updatedFilm.getName());
    }

    @Test
    @DisplayName("Film creation - film not found")
    void filmUpdateNotFound() throws Exception {
        Film film = Film.builder()
                .id(123456789L)
                .name("Test film")
                .description("Test film description")
                .releaseDate(LocalDate.of(1990, 9, 27))
                .duration(Duration.ofMinutes(120))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(film);
        this.mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Film like and unlike - ok scenario")
    void filmLikeAndUnlikeIsOk() throws Exception {
        Film film = Film.builder()
                .id(123456789L)
                .name("Test film")
                .description("Test film description")
                .releaseDate(LocalDate.of(1990, 9, 27))
                .duration(Duration.ofMinutes(120))
                .likes(new HashSet<>())
                .build();

        User user = User.builder()
                .name("")
                .email("john@doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("JDoe")
                .build();

        Film createdFilm = filmStorage.createEntityInStorage(film);
        User createdUser = userStorage.createEntityInStorage(user);
        String url = String.format("/films/%d/like/%d", createdFilm.getId(), createdUser.getId());

        this.mockMvc.perform(put(url))
                .andDo(print())
                .andExpect(status().isOk());

        Film updatedFilm = filmStorage.readEntityFromStorage(createdFilm.getId());

        Assertions.assertTrue(updatedFilm.getLikes().contains(createdUser.getId()));

        this.mockMvc.perform(delete(url))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertTrue(updatedFilm.getLikes().size() == 0);
    }

    @Test
    @DisplayName("Film like and unlike - user not found")
    void filmLikeUserNotFound() throws Exception {
        Film film = Film.builder()
                .id(123456789L)
                .name("Test film")
                .description("Test film description")
                .releaseDate(LocalDate.of(1990, 9, 27))
                .duration(Duration.ofMinutes(120))
                .likes(new HashSet<>())
                .build();


        Film createdFilm = filmStorage.createEntityInStorage(film);

        String url = String.format("/films/%d/like/1234567890", createdFilm.getId());

        this.mockMvc.perform(put(url))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Film like and unlike - film not found")
    void filmLikeFilmNotFound() throws Exception {
        User user = User.builder()
                .name("")
                .email("john@doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("JDoe")
                .build();

        User createdUser = userStorage.createEntityInStorage(user);
        String url = String.format("/films/1234567890/like/%d", createdUser.getId());

        this.mockMvc.perform(put(url))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}