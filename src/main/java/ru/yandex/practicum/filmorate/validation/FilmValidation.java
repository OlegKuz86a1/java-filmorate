package ru.yandex.practicum.filmorate.validation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmBaseDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.GenreEntity;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmValidation {

    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public FilmValidation(MpaStorage mpaStorage, GenreStorage genreStorage,
                          @Qualifier("userDbStorage") UserStorage userStorage,
                          @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public void validateLike(Long userId, Long filmId) {
        if (userStorage.findById(userId).isEmpty() || filmStorage.findById(filmId).isEmpty()) {
            throw new NotFoundException("Пользователь или фильм с таким Id не найден");
        }
    }

    public void validate(FilmBaseDto dto) {
        validateMpa(dto.getMpa());
        validateGenreDto(dto.getGenres());
    }

    public void validateMpa(MpaDto mpaDto) {
        if (mpaDto == null) {
            return;
        }

        mpaStorage.getById(mpaDto.getId()).orElseThrow(() -> new NotFoundException("MPA не существует", true));
    }

    public void validateGenreDto(List<GenreDto> genreDto) {
        if (genreDto == null || genreDto.isEmpty()) {
            return;
        }

        List<Integer> genreIds = genreDto.stream().mapToInt(GenreDto::getId).boxed().distinct().toList();
        List<GenreEntity> genresExists = genreStorage.getByIds(genreIds);

        if (genreIds.size() != genresExists.size()) {
            throw new NotFoundException("Жанр не существует", true);
        }
    }
}
