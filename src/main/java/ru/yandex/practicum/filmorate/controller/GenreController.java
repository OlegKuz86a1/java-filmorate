package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class GenreController {

    private final FilmService filmService;

    @GetMapping("/genres")
    public List<GenreDto> allGenres() {
        log.info("Запрос на получение всех жанров");
        return filmService.allGenres();
    }

    @GetMapping("/genres/{genreId}")
    public GenreDto getGenreById(@PathVariable("genreId") Integer genreId) {
        log.info("Запрос на получение жанра по id {}", genreId);
        return filmService.getGenreById(genreId);
    }
}
