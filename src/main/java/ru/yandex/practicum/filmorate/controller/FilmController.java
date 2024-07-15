package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Long, Film> films = new HashMap<>();

    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleValidationException(ValidationException e) {
        return ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, e.getMessage()).build();
    }

    @GetMapping
    public Collection<Film> allFilms() {
        log.info("Запрос на получение всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Запрос на создание нового фильма: {}", film);
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("название не должно быть пустым");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно создан: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Запрос на обновление фильма: {}", newFilm.getName());
        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            throw new ValidationException("название не должно быть пустым");
        }
        if (films.containsKey(newFilm.getId())) {
           films.put(newFilm.getId(), newFilm);
            log.info("Фильм успешно обновлен: {}", newFilm.getName());
        } else {
            log.warn("Не удалось найти фильм для обновления: {}", newFilm.getName());
        }
        return newFilm;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Генерация нового ID для фильма: текущий максимум - {}, новый ID - {}", currentMaxId, ++currentMaxId);
        return currentMaxId;
    }
}
