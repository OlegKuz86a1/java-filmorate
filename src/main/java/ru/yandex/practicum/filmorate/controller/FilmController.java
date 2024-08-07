package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> allFilms() {
        log.info("Запрос на получение всех фильмов");
        return filmStorage.allFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Запрос на создание нового фильма: {}", film);
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Запрос на обновление фильма: {}", newFilm.getName());
        return filmStorage.update(newFilm);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void likeFilm(@PathVariable("filmId")Long filmId,
                         @PathVariable("userId") Long userId) {
        filmService.likeFilm(userId, filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void unlikeFilm(@PathVariable("filmId")Long filmId,
                           @PathVariable("userId") Long userId) {
        filmService.unlikeFilm(userId, filmId);
    }

    @GetMapping("/popular")
    public List<Film> topLikedFilms(@RequestParam(defaultValue = "10")int count) {
        return filmService.topLikedFilms(count);
    }
}
