package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaEntity;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<MpaEntity> MPA_ENTITY_ROW_MAPPER = (rs, rn) -> MpaEntity.builder()
            .id(rs.getInt("id"))
            .name(rs.getString("name"))
            .build();

    @Override
    public Optional<MpaEntity> getById(Integer id) {
        List<MpaEntity> mpa = jdbcTemplate.query(
                "SELECT * FROM film_rating WHERE id = ?", MPA_ENTITY_ROW_MAPPER, id
        );

        return Optional.ofNullable(mpa.isEmpty() ? null : mpa.getFirst());
    }

    @Override
    public List<MpaEntity> getAll() {
        return jdbcTemplate.query("SELECT * FROM film_rating", MPA_ENTITY_ROW_MAPPER);
    }
}
