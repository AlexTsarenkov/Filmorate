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
    private Long id;
    @Email(message = "Email format is invalid")
    private String email;
    @NotBlank(message = "Login can not be blank")
    @Pattern(regexp = "^\\S+$", message = "Login can not contain spaces")
    private String login;
    private String name;
    @PastOrPresent(message = "Birthday date can't be in future")
    private LocalDate birthday;
}
