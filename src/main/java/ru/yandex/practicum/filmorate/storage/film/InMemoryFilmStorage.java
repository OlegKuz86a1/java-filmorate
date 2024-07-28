package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> allFilms() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("название не должно быть пустым");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно создан: {}", film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            throw new ValidationException("название не должно быть пустым");
        }

        if (!films.containsKey(newFilm.getId())) {
            log.warn("Не удалось найти фильм для обновления: {}", newFilm.getName());
            throw new NotFoundException("Не найден фильм");
        }
        films.put(newFilm.getId(), newFilm);
        log.info("Фильм успешно обновлен: {}", newFilm.getName());
        return newFilm;
    }

    @Override
    public Optional<Film> findById(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("Ожидается id фильма");
        }
        return films.entrySet().stream()
                .filter(entry -> entry.getKey().equals(filmId))
                .map(Map.Entry::getValue)
                .findFirst();
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
