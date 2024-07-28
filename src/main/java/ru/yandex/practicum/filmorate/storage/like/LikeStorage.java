package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {
    void likeFilm(Long userId, Long filmId);

    void unlikeFilm(Long userId, Long filmId);

    List<Film> topLikedFilms(int limit);
}
