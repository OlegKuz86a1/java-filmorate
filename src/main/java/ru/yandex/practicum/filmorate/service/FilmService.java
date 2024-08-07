package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmService  {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(LikeStorage likeStorage, FilmStorage filmStorage, UserStorage userStorage) {
        this.likeStorage = likeStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void likeFilm(Long userId, Long filmId) {
        if (userStorage.findById(userId).isEmpty() || filmStorage.findById(filmId).isEmpty()) {
            throw new NotFoundException("Пользователь или фильм с таким Id не найден");
        }
        likeStorage.likeFilm(userId, filmId);
    }

    public void unlikeFilm(Long userId, Long filmId) {
        if (userStorage.findById(userId).isEmpty() || filmStorage.findById(filmId).isEmpty()) {
            throw new NotFoundException("Пользователь или фильм с таким Id не найден");
        }
        likeStorage.unlikeFilm(userId, filmId);
    }

    public List<Film> topLikedFilms(int count) {
        return likeStorage.topLikedFilms(count);
    }
}
