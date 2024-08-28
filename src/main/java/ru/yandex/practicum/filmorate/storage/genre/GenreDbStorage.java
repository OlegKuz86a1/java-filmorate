package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.GenreEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<GenreEntity> GENRE_ENTITY_ROW_MAPPER = (rs, rn) -> GenreEntity.builder()
            .id(rs.getInt("genre_id"))
            .name(rs.getString("name_genre"))
            .build();

    @Override
    public Optional<GenreEntity> getById(Integer id) {
        List<GenreEntity> genres = jdbcTemplate.query(
                "SELECT * FROM genre WHERE genre_id = ?", GENRE_ENTITY_ROW_MAPPER, id
        );

        return Optional.ofNullable(genres.isEmpty() ? null : genres.getFirst());
    }

    @Override
    public List<GenreEntity> getAll() {
        return jdbcTemplate.query("SELECT * FROM genre", GENRE_ENTITY_ROW_MAPPER);
    }

    @Override
    public List<GenreEntity> getByIds(List<Integer> ids) {
        return jdbcTemplate.query("SELECT * FROM genre WHERE genre_id IN (%s)".formatted(IntStream.range(0, ids.size())
                        .mapToObj(n -> "?").collect(Collectors.joining(","))), GENRE_ENTITY_ROW_MAPPER,
                ids.toArray());
    }

    @Override
    public List<GenreEntity> getByFilmId(Long filmId) {
        return jdbcTemplate.query(
                "SELECT genre.* FROM film_genre " +
                        "LEFT JOIN genre USING (genre_id) " +
                        "WHERE film_id = ?", GENRE_ENTITY_ROW_MAPPER, filmId);
    }

    @Override
    public void addGenresToFilm(Long filmId, List<Integer> genres) {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO film_genre (film_id, genre_id) VALUES");
        sqlBuilder.append(genres.stream()
                .map(genreId -> "(%d, %d)".formatted(filmId, genreId))
                .collect(Collectors.joining(",")));

        jdbcTemplate.update(sqlBuilder.toString());
    }

    @Override
    public void deleteGenresByFilmId(Long filmId) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
    }
}
