package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.Duration;
import java.time.LocalDate;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
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

}