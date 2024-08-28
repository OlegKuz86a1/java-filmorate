package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dto.FilmBaseDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.util.List;

@Service
public class FilmService  {

    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final FilmMapper filmMapper;
    private final FilmValidation filmValidation;

    public FilmService(@Qualifier(value = "filmDbStorage") FilmStorage filmStorage, GenreStorage genreStorage,
                       MpaStorage mpaStorage, FilmMapper filmMapper, FilmValidation filmValidation) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.filmMapper = filmMapper;
        this.filmValidation = filmValidation;
    }

    public void likeFilm(Long userId, Long filmId) {
        filmValidation.validateLike(userId, filmId);
        filmStorage.likeFilm(userId, filmId);
    }

    public void unlikeFilm(Long userId, Long filmId) {
        filmValidation.validateLike(userId, filmId);
        filmStorage.unlikeFilm(userId, filmId);
    }

    public List<FilmDto> topLikedFilms(int count) {
        return filmMapper.toDto(filmStorage.topLikedFilms(count)).stream()
                .peek(filmDto -> filmDto.setGenres(filmMapper.mapToGenreDto(genreStorage.getByFilmId(filmDto.getId()))))
                .toList();
    }

    public List<FilmDto> allFilms() {
        return filmMapper.toDto(filmStorage.allFilms()).stream()
                .peek(filmDto -> filmDto.setGenres(filmMapper.mapToGenreDto(genreStorage.getByFilmId(filmDto.getId()))))
                .toList();
    }

    public FilmDto create(FilmBaseDto filmBaseDto) {
        filmValidation.validate(filmBaseDto);
        FilmDto dto = filmMapper.toDto(filmStorage.create(filmMapper.mapToEntity(filmBaseDto)));

        if (filmBaseDto.getGenres() != null && !filmBaseDto.getGenres().isEmpty()) {
            genreStorage.addGenresToFilm(dto.getId(), filmBaseDto.getGenres().stream().mapToInt(GenreDto::getId).boxed()
                    .distinct().toList());
            dto.setGenres(filmMapper.mapToGenreDto(genreStorage.getByFilmId(dto.getId())));
        }

        return dto;
    }

    public FilmDto update(@Valid @RequestBody FilmDto filmDto) {
        filmValidation.validate(filmDto);
        filmStorage.findById(filmDto.getId())
                .orElseThrow(() -> new NotFoundException("Фильм с таким Id не найден"));
        FilmDto dto = filmMapper.toDto(filmStorage.update(filmMapper.toEntity(filmDto)));

        if (filmDto.getGenres() != null && !filmDto.getGenres().isEmpty()) {
            genreStorage.deleteGenresByFilmId(filmDto.getId());
            genreStorage.addGenresToFilm(filmDto.getId(), filmDto.getGenres().stream().mapToInt(GenreDto::getId).boxed().toList());
        } else {
            dto.setGenres(filmMapper.mapToGenreDto(genreStorage.getByFilmId(filmDto.getId())));
        }

        return dto;
    }

    public FilmDto getById(@PathVariable("filmId") Long filmId) {
        return filmMapper.toDto(filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с таким Id не найден")),
                filmMapper.mapToGenreDto(genreStorage.getByFilmId(filmId)));
    }

    public List<GenreDto> allGenres() {
        return genreStorage.getAll().stream()
                .map(filmMapper::mapToGenreDto)
                .toList();
    }

    public GenreDto getGenreById(@PathVariable("genreId") Integer genreId) {
        return filmMapper.mapToGenreDto(genreStorage.getById(genreId)
                .orElseThrow(() -> new NotFoundException("Жанр с таким Id не найден")));
    }

    public List<MpaDto> allMpa() {
        return mpaStorage.getAll().stream()
                .map(filmMapper::mapToMpaDto)
                .toList();
    }

    public MpaDto getMpaById(@PathVariable("mpaId") Integer mpaId) {
        return filmMapper.mapToMpaDto(mpaStorage.getById(mpaId)
                .orElseThrow(() -> new NotFoundException("MPA с таким Id не найден")));
    }
}
