package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryLikeStorage implements LikeStorage {

    private final Map<Long, Integer> likeCounts = new HashMap<>();
    private final Map<Long, Set<Long>> likes = new HashMap<>();
    private final FilmStorage filmStorage;

    @Autowired
    public InMemoryLikeStorage(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public void likeFilm(Long userId, Long filmId) {
        if (userId == null || filmId == null) {
            throw new ValidationException("id пользователя и id фильма должны быть указаны");
        }
        likes.computeIfAbsent(userId, k -> new HashSet<>()).add(filmId);
        likeCounts.put(filmId, likeCounts.getOrDefault(filmId, 0) + 1);
    }

    @Override
    public void unlikeFilm(Long userId, Long filmId) {
        if (userId == null || filmId == null) {
            throw new ValidationException("id пользователя и id фильма должны быть указаны");
        }
        likes.computeIfAbsent(userId, k -> new HashSet<>()).remove(filmId);
        likeCounts.put(filmId, likeCounts.getOrDefault(filmId, 0) - 1);
    }

    @Override
    public List<Film> topLikedFilms(int count) {
        return likeCounts.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer> comparingByValue().reversed())
                .limit(count)
                .map(Map.Entry::getKey)
                .map(this::getFilmById)
                .collect(Collectors.toList());
    }

    private Film getFilmById(Long id) {
        return filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }
}
