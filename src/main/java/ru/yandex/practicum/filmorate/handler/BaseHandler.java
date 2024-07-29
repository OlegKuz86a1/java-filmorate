package ru.yandex.practicum.filmorate.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Service
public class BaseHandler implements FilmBaseHandler {
    public final FilmStorage filmStorage;

    @Autowired
    public BaseHandler(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Collection<Film> allFilms() {
        return filmStorage.allFilms();
    }
}
