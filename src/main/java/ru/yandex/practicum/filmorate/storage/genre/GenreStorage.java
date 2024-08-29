package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.GenreEntity;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    Optional<GenreEntity> getById(Integer id);

    List<GenreEntity> getAll();

    List<GenreEntity> getByIds(List<Integer> ids);

    List<GenreEntity> getByFilmId(Long filmId);

    void addGenresToFilm(Long filmId, List<Integer> genres);

    void deleteGenresByFilmId(Long filmId);
}
