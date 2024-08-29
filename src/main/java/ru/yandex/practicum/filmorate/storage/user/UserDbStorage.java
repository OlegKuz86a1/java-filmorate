package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.UserEntity;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<UserEntity> USER_ENTITY_ROW_MAPPER = (rs, rn) -> UserEntity.builder()
            .id(rs.getLong("user_id"))
            .name(rs.getString("name"))
            .email(rs.getString("email"))
            .birthday(rs.getDate("birthday").toLocalDate())
            .login(rs.getString("login"))
            .build();

    @Override
    public List<UserEntity> allUsers() {
        return jdbcTemplate.query("SELECT * FROM users", USER_ENTITY_ROW_MAPPER);
    }

    @Override
    public UserEntity create(UserEntity userEntity) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) SELECT ?, ?, ?, ?";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, userEntity.getEmail());
            stmt.setString(2, userEntity.getLogin());
            stmt.setString(3, userEntity.getName());
            stmt.setDate(4, Date.valueOf(userEntity.getBirthday()));
            return stmt;
        }, keyHolder);

        userEntity.setId(keyHolder.getKeyAs(Long.class));

        return userEntity;
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        jdbcTemplate.update(
                "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? " +
                        "WHERE user_id = ?",
                userEntity.getEmail(),
                userEntity.getLogin(),
                userEntity.getName(),
                userEntity.getBirthday(),
                userEntity.getId());
        return userEntity;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sqlQuery = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setLong(1, userId);
            stmt.setLong(2, friendId);
            return stmt;
        });
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM friends WHERE user_id = ? AND friend_id = ?");
            stmt.setLong(1, userId);
            stmt.setLong(2, friendId);
            return stmt;
        });
    }

    @Override
    public List<UserEntity> getFriends(Long userId) {
        return jdbcTemplate.query(
                "SELECT users.* FROM friends LEFT JOIN users ON friends.friend_id = users.user_id " +
                        "WHERE friends.user_id = ?", USER_ENTITY_ROW_MAPPER, userId);
    }

    @Override
    public Optional<UserEntity> findById(Long userId) {
        List<UserEntity> users = jdbcTemplate.query(
                "SELECT * FROM users WHERE user_id = ?", USER_ENTITY_ROW_MAPPER, userId);

        return Optional.ofNullable(users.isEmpty() ? null : users.getFirst());
    }

    @Override
    public List<UserEntity> getCommonFriends(Long userId, Long otherUserId) {
        return jdbcTemplate.query(
                "SELECT users.*, friend_id, COUNT(*) AS count FROM friends " +
                        "LEFT JOIN users ON users.user_id = friends.friend_id " +
                        "WHERE friends.user_id IN (?, ?) " +
                        "GROUP BY friend_id HAVING count > 1", USER_ENTITY_ROW_MAPPER, userId, otherUserId);
    }
}
