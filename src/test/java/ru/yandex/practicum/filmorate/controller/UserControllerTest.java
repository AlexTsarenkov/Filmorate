package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void userCreationOk() throws Exception {
        User user = User.builder()
                .name("John Doe")
                .email("john@doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("JDoe")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void userCreationEmptyLogin() throws Exception {
        User user = User.builder()
                .name("John Doe")
                .email("john@doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void userCreationWrongEmail() throws Exception {
        User user = User.builder()
                .name("John Doe")
                .email("john_doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("Jdoe")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void userCreationSpaceInLogin() throws Exception {
        User user = User.builder()
                .name("John Doe")
                .email("john@doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("J doe")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void userCreationBirthdateInFuture() throws Exception {
        User user = User.builder()
                .name("John Doe")
                .email("john@doe.com")
                .birthday(LocalDate.of(3900, 9, 27))
                .login("Jdoe")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void userEmptyName() throws Exception {
        User user = User.builder()
                .name("")
                .email("john@doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("JDoe")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(user);

        MvcResult result = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        User createdUser = mapper.readValue(content, User.class);

        Assert.assertEquals("JDoe", createdUser.getName());
    }

    @Test
    void updateIsOk() throws Exception {
        User user = User.builder()
                .name("")
                .email("john@doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("JDoe")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(user);

        MvcResult result = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        User createdUser = mapper.readValue(content, User.class);

        createdUser.setName("Sam Smith");
        jsonString = mapper.writeValueAsString(createdUser);

        result = this.mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        content = result.getResponse().getContentAsString();
        User updatedUser = mapper.readValue(content, User.class);

        Assert.assertEquals("Sam Smith", updatedUser.getName());
    }

    @Test
    void updateNotFound() throws Exception {
        User user = User.builder()
                .id(12345L)
                .name("")
                .email("john@doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("JDoe")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        String jsonString = mapper.writeValueAsString(user);
        this.mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}