package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmEntity;
import ru.yandex.practicum.filmorate.model.MpaEntity;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<FilmEntity> FILM_ENTITY_ROW_MAPPER = (rs, rn) -> FilmEntity.builder()
            .id(rs.getLong("film_id"))
            .name(rs.getString("film_name"))
            .description(rs.getString("description"))
            .releaseDate(rs.getDate("release_date").toLocalDate())
            .duration(rs.getInt("duration"))
            .mpa(MpaEntity.builder().id(rs.getInt("film_rating.id"))
                    .name(rs.getString("film_rating.name")).build())
            .countLikes(rs.getLong("count_likes"))
            .build();

    @Override
    public List<FilmEntity> allFilms() {
        return jdbcTemplate.query(
                "SELECT films.*, film_rating.*, COALESCE(film_likes.count_likes, 0) as count_likes FROM films " +
                        "LEFT JOIN film_rating ON films.rating_id = film_rating.id " +
                        "LEFT JOIN (" +
                        "SELECT film_id, COUNT(*) as count_likes FROM likes " +
                        "GROUP BY film_id" +
                        ") as film_likes ON film_likes.film_id = films.film_id", FILM_ENTITY_ROW_MAPPER);
    }

    @Override
    public FilmEntity create(FilmEntity filmEntity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO films(film_name, description, release_date, duration, rating_id) SELECT ?, ?, ?, ?, ?",
                    new String[]{"film_id"});
            stmt.setString(1, filmEntity.getName());
            stmt.setString(2, filmEntity.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(filmEntity.getReleaseDate()));
            stmt.setLong(4, filmEntity.getDuration());
            stmt.setInt(5, filmEntity.getMpa().getId());
            return stmt;
        }, keyHolder);

        filmEntity.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return filmEntity;
    }

    @Override
    public FilmEntity update(FilmEntity newFilmEntity) {
        jdbcTemplate.update(
                "UPDATE films SET film_name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                        "WHERE film_id = ?",
                newFilmEntity.getName(),
                newFilmEntity.getDescription(),
                java.sql.Date.valueOf(newFilmEntity.getReleaseDate()),
                newFilmEntity.getDuration(),
                newFilmEntity.getMpa().getId(),
                newFilmEntity.getId());

        return newFilmEntity;
    }

    @Override
    public Optional<FilmEntity> findById(Long filmId) {
        List<FilmEntity> films = jdbcTemplate.query(
                "SELECT films.*, film_rating.*, COALESCE(film_likes.count_likes, 0) as count_likes FROM films " +
                        "LEFT JOIN film_rating ON films.rating_id = film_rating.id " +
                        "LEFT JOIN (SELECT film_id, COUNT(*) as count_likes FROM likes GROUP BY film_id) as film_likes " +
                        "WHERE films.film_id = ?", FILM_ENTITY_ROW_MAPPER, filmId
        );
        return Optional.ofNullable(films.isEmpty() ? null : films.getFirst());
    }

    @Override
    public List<FilmEntity> topLikedFilms(int limit) {
        return jdbcTemplate.query(
                "SELECT films.*, film_rating.*, COALESCE(film_likes.count_likes, 0) as count_likes FROM films " +
                        "LEFT JOIN film_rating ON films.rating_id = film_rating.id " +
                        "LEFT JOIN (" +
                        "SELECT film_id, COUNT(user_id) as count_likes FROM likes " +
                        "GROUP BY film_id" +
                        ") as film_likes ON film_likes.film_id = films.film_id " +
                        "ORDER BY film_likes.count_likes DESC LIMIT ?", FILM_ENTITY_ROW_MAPPER, limit);
    }

    @Override
    public void likeFilm(Long userId, Long filmId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO likes (film_id, user_id) SELECT ?, ?");
            stmt.setLong(1, filmId);
            stmt.setLong(2, userId);
            return stmt;
        });
    }

    @Override
    public void unlikeFilm(Long userId, Long filmId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM likes WHERE film_id = ? AND user_id = ?");
            stmt.setLong(1, filmId);
            stmt.setLong(2, userId);
            return stmt;
        });
    }
}
