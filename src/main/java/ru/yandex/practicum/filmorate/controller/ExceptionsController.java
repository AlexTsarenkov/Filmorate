package ru.yandex.practicum.filmorate.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.dto.ExceptionDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.LocalDateTime;


@RestControllerAdvice
@Slf4j
public class ExceptionsController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody ResponseEntity<ExceptionDto> validationError(HttpServletRequest req,
                                                                      MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ExceptionDto exDto = ExceptionDto.builder()
                .url(req.getRequestURI())
                .message(msg)
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.BAD_REQUEST)
                .build();
        log.error(msg);
        return new ResponseEntity<>(exDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody ResponseEntity<ExceptionDto> notFound(HttpServletRequest req,
                                                               NotFoundException ex) {
        ExceptionDto exDto = ExceptionDto.builder()
                .url(req.getRequestURI())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.NOT_FOUND)
                .build();
        log.error(ex.getMessage());
        return new ResponseEntity<>(exDto, HttpStatus.NOT_FOUND);
    }

}
