package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmBaseDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<FilmDto> allFilms() {
        log.info("Запрос на получение всех фильмов");
        return filmService.allFilms();
    }

    @GetMapping("/{filmId}")
    public FilmDto getById(@PathVariable("filmId") Long filmId) {
        log.info("Запрос на получение фильма по ID");
        return filmService.getById(filmId);
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody FilmBaseDto filmDto) {
        log.info("Запрос на создание нового фильма: {}", filmDto);
        return filmService.create(filmDto);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody FilmDto filmDto) {
        log.info("Запрос на обновление фильма: {}", filmDto.getName());
        return filmService.update(filmDto);
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
    public List<FilmDto> topLikedFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.topLikedFilms(count);
    }
}
