package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.FilmEntity;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<FilmEntity> allFilms();

    List<FilmEntity> topLikedFilms(int limit);

    FilmEntity create(FilmEntity filmEntity);

    FilmEntity update(FilmEntity newFilmEntity);

    Optional<FilmEntity> findById(Long filmId);

    void likeFilm(Long userId, Long filmId);

    void unlikeFilm(Long userId, Long filmId);
}
