package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryStorageCRUD;

import java.time.LocalDate;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemoryStorageCRUD<User> userStorage;

    @Test
    @DisplayName("User creation - ok scenario")
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
    @DisplayName("User creation - empty login")
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
    @DisplayName("User creation - wrong email")
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
    @DisplayName("User creation - login wrong pattern")
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
    @DisplayName("User creation - birthday in future")
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
    @DisplayName("User creation - empty name")
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
    @DisplayName("User update - ok scenario")
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
    @DisplayName("User update - user not found")
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

    @Test
    @DisplayName("User friends - add and remove is ok")
    void userAddAndRemoveFriendIsOk() throws Exception {
        User userOne = User.builder()
                .name("")
                .email("john@doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("JDoe")
                .friends(new HashSet<>())
                .build();

        User userTwo = User.builder()
                .name("")
                .email("sam@doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("SDoe")
                .friends(new HashSet<>())
                .build();

        User createdUserOne = userStorage.createEntityInStorage(userOne);
        User createdUserTwo = userStorage.createEntityInStorage(userTwo);
        String uri = String.format("/users/%s/friends/%s", createdUserOne.getId(), createdUserTwo.getId());
        this.mockMvc.perform(put(uri))
                .andDo(print())
                .andExpect(status().isOk());

        createdUserOne = userStorage.readEntityFromStorage(createdUserOne.getId());
        createdUserTwo = userStorage.readEntityFromStorage(createdUserTwo.getId());

        Assert.assertTrue(createdUserOne.getFriends().contains(createdUserTwo.getId()));
        Assert.assertTrue(createdUserTwo.getFriends().contains(createdUserOne.getId()));

        uri = String.format("/users/%s/friends/%s", createdUserOne.getId(), createdUserTwo.getId());
        this.mockMvc.perform(delete(uri))
                .andDo(print())
                .andExpect(status().isOk());

        Assert.assertTrue(createdUserOne.getFriends().size() == 0);
        Assert.assertTrue(createdUserTwo.getFriends().size() == 0);
    }

    @Test
    @DisplayName("User friends - user not found")
    void userAddFriendNotFound() throws Exception {
        User userOne = User.builder()
                .name("")
                .email("john@doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("JDoe")
                .friends(new HashSet<>())
                .build();

        User createdUserOne = userStorage.createEntityInStorage(userOne);

        String uri = String.format("/users/%s/friends/1234567890", createdUserOne.getId());
        this.mockMvc.perform(put(uri))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("User friends remove - user not found")
    void userDeleteFriendNotFound() throws Exception {
        User userOne = User.builder()
                .name("")
                .email("john@doe.com")
                .birthday(LocalDate.of(1990, 9, 27))
                .login("JDoe")
                .friends(new HashSet<>())
                .build();

        User createdUserOne = userStorage.createEntityInStorage(userOne);

        String uri = String.format("/users/%s/friends/1234567890", createdUserOne.getId());
        this.mockMvc.perform(delete(uri))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}