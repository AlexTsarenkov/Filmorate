package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ExceptionDto {
    public String url;
    public String message;
    public HttpStatusCode statusCode;
    public LocalDateTime timestamp;
}
