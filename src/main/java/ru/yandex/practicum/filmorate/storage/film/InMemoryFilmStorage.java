package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.FilmEntity;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, FilmEntity> films = new HashMap<>();
    private final Map<Long, Set<Long>> likes = new HashMap<>();

    @Override
    public List<FilmEntity> allFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public FilmEntity create(FilmEntity filmEntity) {
        if (filmEntity.getName() == null || filmEntity.getName().isBlank()) {
            throw new ValidationException("название не должно быть пустым");
        }
        filmEntity.setId(getNextId());
        films.put(filmEntity.getId(), filmEntity);
        log.info("Фильм успешно создан: {}", filmEntity);
        return filmEntity;
    }

    @Override
    public FilmEntity update(FilmEntity newFilmEntity) {
        if (newFilmEntity.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        if (newFilmEntity.getName() == null || newFilmEntity.getName().isBlank()) {
            throw new ValidationException("название не должно быть пустым");
        }

        if (!films.containsKey(newFilmEntity.getId())) {
            log.warn("Не удалось найти фильм для обновления: {}", newFilmEntity.getName());
            throw new NotFoundException("Не найден фильм");
        }
        films.put(newFilmEntity.getId(), newFilmEntity);
        log.info("Фильм успешно обновлен: {}", newFilmEntity.getName());
        return newFilmEntity;
    }

    @Override
    public Optional<FilmEntity> findById(Long filmId) {
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

    @Override
    public List<FilmEntity> topLikedFilms(int count) {
        return allFilms().stream()
                .peek(film -> film.setCountLikes((long) likes.getOrDefault(film.getId(), new HashSet<>()).size()))
                .sorted(Comparator.comparing(FilmEntity::getCountLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
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
}
