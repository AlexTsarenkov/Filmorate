package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class User {
    Long id;
    @Email(message = "Email format is invalid")
    String email;
    @NotBlank(message = "Login can not be blank")
    @Pattern(regexp = "^\\S+$", message = "Login can not contain spaces")
    String login;
    String name;
    @PastOrPresent(message = "Birthday date can't be in future")
    LocalDate birthday;
}
