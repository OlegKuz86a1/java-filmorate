package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MpaController {

    private final FilmService filmService;

    @GetMapping("/mpa/{mpaId}")
    public MpaDto getMpaById(@PathVariable("mpaId") Integer mpaId) {
        log.info("Запрос на получение mpa по id {}", mpaId);
        return filmService.getMpaById(mpaId);
    }

    @GetMapping("/mpa")
    public List<MpaDto> allMpa() {
        log.info("Запрос на получение всех mpa");
        return filmService.allMpa();
    }
}
