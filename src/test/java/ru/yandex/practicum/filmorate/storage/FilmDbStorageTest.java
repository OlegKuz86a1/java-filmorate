package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.FilmEntity;
import ru.yandex.practicum.filmorate.model.GenreEntity;
import ru.yandex.practicum.filmorate.model.MpaEntity;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Sql({"/schema.sql", "/test-data.sql"})
@ComponentScan("ru.yandex.practicum.filmorate")
public class FilmDbStorageTest {

    @Autowired
    private FilmStorage filmStorage;

    private final static MpaEntity MPA_CREATED = new MpaEntity(4, null);

    private final static MpaEntity MPA_UPDATED = new MpaEntity(3, null);

    private final static FilmEntity FILM_CREATED = FilmEntity.builder()
            .name("Test_created_name")
            .description("Test_created_description")
            .duration(101)
            .releaseDate(LocalDate.of(2024, 3, 2))
            .genres(List.of(new GenreEntity(4, null), new GenreEntity(5, null)))
            .mpa(MPA_CREATED)
            .build();

    private final static FilmEntity FILM_UPDATED = FilmEntity.builder()
            .id(6L)
            .name("Test_updated_name")
            .description("Test_updated_description")
            .duration(999)
            .releaseDate(LocalDate.of(2020, 6, 7))
            .genres(List.of(new GenreEntity(6, null), new GenreEntity(7, null)))
            .mpa(MPA_UPDATED)
            .build();

    @Test
    public void testAllFilms() {
        List<FilmEntity> filmEntities = filmStorage.allFilms();

        assertThat(filmEntities)
                .hasSize(5);
    }

    @Test
    public void create() {
        FilmEntity filmEntityCreated = filmStorage.create(FILM_CREATED);

        assertThat(filmEntityCreated)
                .hasFieldOrPropertyWithValue("id", 6L)
                .hasFieldOrPropertyWithValue("name", "Test_created_name")
                .hasFieldOrPropertyWithValue("description", "Test_created_description")
                .hasFieldOrPropertyWithValue("duration", 101)
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2024, 3, 2))
                .hasFieldOrPropertyWithValue("genres", List.of(new GenreEntity(4, null), new GenreEntity(5, null)))
                .hasFieldOrPropertyWithValue("mpa", MPA_CREATED);
    }
    @Test
    public void update() {
        filmStorage.create(FILM_CREATED);
        FilmEntity updated = filmStorage.update(FILM_UPDATED);

        assertThat(updated)
                .hasFieldOrPropertyWithValue("id", 6L)
                .hasFieldOrPropertyWithValue("name", "Test_updated_name")
                .hasFieldOrPropertyWithValue("description", "Test_updated_description")
                .hasFieldOrPropertyWithValue("duration", 999)
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2020, 6, 7))
                .hasFieldOrPropertyWithValue("genres", List.of(new GenreEntity(6, null), new GenreEntity(7, null)))
                .hasFieldOrPropertyWithValue("mpa", MPA_UPDATED);
    }

    @Test
    public void findById() {
        Optional<FilmEntity> filmOptional = filmStorage.findById(3L);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 3L)
                );
    }

    @Test
    public void topLikedFilms() {
        List<FilmEntity> filmEntities = filmStorage.topLikedFilms(3);

        assertThat(filmEntities)
                .hasSize(3);

        assertThat(filmEntities.get(0)).hasFieldOrPropertyWithValue("id", 3L);
        assertThat(filmEntities.get(0)).hasFieldOrPropertyWithValue("countLikes", 3L);
        assertThat(filmEntities.get(1)).hasFieldOrPropertyWithValue("id", 5L);
        assertThat(filmEntities.get(1)).hasFieldOrPropertyWithValue("countLikes", 2L);
        assertThat(filmEntities.get(2)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(filmEntities.get(2)).hasFieldOrPropertyWithValue("countLikes", 1L);
    }

    @Test
    public void likeFilm() {
        Long createdFilmId = filmStorage.create(FILM_CREATED).getId();
        filmStorage.likeFilm(1L, createdFilmId);

        Optional<FilmEntity> filmEntity = filmStorage.findById(createdFilmId);

        assertThat(filmEntity)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", createdFilmId))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("countLikes", 1L));
    }

    @Test
    public void unlikeFilm() {
        Long createdFilmId = filmStorage.create(FILM_CREATED).getId();
        filmStorage.likeFilm(2L, createdFilmId);

        Optional<FilmEntity> filmEntityWithLike = filmStorage.findById(createdFilmId);

        assertThat(filmEntityWithLike)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", createdFilmId))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("countLikes", 1L));

        filmStorage.unlikeFilm(2L, createdFilmId);

        Optional<FilmEntity> filmEntityWithoutLike = filmStorage.findById(createdFilmId);

        assertThat(filmEntityWithoutLike)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", createdFilmId))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("countLikes", 0L));
    }
}
