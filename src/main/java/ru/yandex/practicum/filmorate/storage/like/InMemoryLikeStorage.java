package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.handler.FilmBaseHandler;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryLikeStorage implements LikeStorage {

    private final Map<Long, Set<Long>> likes = new HashMap<>();
    private final FilmBaseHandler baseHandler;

    @Autowired
    public InMemoryLikeStorage(FilmBaseHandler baseHandler) {
        this.baseHandler = baseHandler;
    }

    @Override
    public void likeFilm(Long userId, Long filmId) {
        if (userId == null || filmId == null) {
            throw new ValidationException("id пользователя и id фильма должны быть указаны");
        }
        likes.computeIfAbsent(filmId, v -> new HashSet<>()).add(userId);
    }

    @Override
    public void unlikeFilm(Long userId, Long filmId) {
        if (userId == null || filmId == null) {
            throw new ValidationException("id пользователя и id фильма должны быть указаны");
        }
        likes.computeIfAbsent(filmId, v -> new HashSet<>()).remove(userId);
    }

    @Override
    public List<Film> topLikedFilms(int count) {
        return baseHandler.allFilms().stream()
                .peek(film -> film.setCountLikes((long) likes.getOrDefault(film.getId(), new HashSet<>()).size()))
                .sorted(Comparator.comparing(Film::getCountLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
