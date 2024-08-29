package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.UserEntity;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@Sql({"/schema.sql", "/test-data.sql"})
@ComponentScan("ru.yandex.practicum.filmorate")
class UserDbStorageTest {

    @Autowired
    private UserStorage userStorage;

    @Test
    public void testAllUsers() {
        List<UserEntity> users = userStorage.allUsers();

        assertThat(users)
                .hasSize(5)
                .containsAll(List.of(
                        new UserEntity(1L, "andrew@yandex.ru", "andrew", "User_andrew", LocalDate.of(1990, 1, 1)),
                        new UserEntity(2L, "petr@yandex.ru", "petr", "User_petr", LocalDate.of(1993, 3, 13)),
                        new UserEntity(3L, "ivan@yandex.ru", "ivan", "User_ivan", LocalDate.of(1992, 2, 2)),
                        new UserEntity(4L, "gena@yandex.ru", "gena", "User_gena", LocalDate.of(1999, 9, 19)),
                        new UserEntity(5L, "drani@yandex.ru", "drani", "User_drani", LocalDate.of(2000, 11, 11))));
    }

    @Test
    public void testFindUserById() {
        Optional<UserEntity> userOptional = userStorage.findById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testCreate() {
        UserEntity newUserEntity = UserEntity.builder()
                .name("Test-User")
                .email("test@mail.ru")
                .login("nnntest")
                .birthday(LocalDate.of(1990, 2, 13))
                .build();

        userStorage.create(newUserEntity);

        Optional<UserEntity> userOptional = userStorage.findById(6L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 6L)
                );
    }

    @Test
    public void testUpdate() {
        UserEntity beforeUserEntity = UserEntity.builder()
                .name("Test-User")
                .email("test@mail.ru")
                .login("nnntest")
                .birthday(LocalDate.of(1990, 2, 13))
                .build();

        userStorage.create(beforeUserEntity);

        Optional<UserEntity> userOptional = userStorage.findById(6L);

        assertThat(userOptional).isPresent();

        UserEntity afterUserEntity = UserEntity.builder()
                .id(6L)
                .name("Updated-User")
                .email("upd@mail.ru")
                .login("upd")
                .birthday(LocalDate.of(1999, 12, 9))
                .build();

        userStorage.update(afterUserEntity);

        Optional<UserEntity> userOptionalUpd = userStorage.findById(6L);

        assertThat(userOptionalUpd)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 6L))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", "Updated-User"))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("login", "upd"))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("email", "upd@mail.ru"))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1999, 12, 9)));
    }

    @Test
    public void testRemoveFriend() {
        userStorage.addFriend(1L, 2L);

        List<UserEntity> friendsBefore = userStorage.getFriends(1L);

        assertThat(friendsBefore)
                .hasSize(1);

        userStorage.removeFriend(1L, 2L);

        List<UserEntity> friendsAfter = userStorage.getFriends(1L);

        assertThat(friendsAfter)
                .hasSize(0);
    }

    @Test
    public void testAddFriend() {
         userStorage.addFriend(1L, 2L);

        List<UserEntity> friends = userStorage.getFriends(1L);

        assertThat(friends)
                .hasSize(1)
                .containsExactly(new UserEntity(2L, "petr@yandex.ru", "petr", "User_petr",
                        LocalDate.of(1993, 3, 13)));
    }

    @Test
    public void testGetFriends() {
        userStorage.addFriend(1L, 2L);
        userStorage.addFriend(1L, 3L);
        userStorage.addFriend(1L, 5L);

        List<UserEntity> friends = userStorage.getFriends(1L);

        assertThat(friends)
                .hasSize(3)
                .containsAll(List.of(new UserEntity(2L, "petr@yandex.ru", "petr", "User_petr",
                        LocalDate.of(1993, 3, 13)),
                        new UserEntity(3L, "ivan@yandex.ru", "ivan", "User_ivan",
                                LocalDate.of(1992, 2, 2)),
                        new UserEntity(5L, "drani@yandex.ru", "drani", "User_drani",
                                LocalDate.of(2000, 11, 11))));//скопировать из sql
    }
    @Test
    public void testGetCommonFriends() {
        userStorage.addFriend(1L, 2L);
        userStorage.addFriend(1L, 3L);
        userStorage.addFriend(1L, 5L);

        userStorage.addFriend(2L, 3L);
        userStorage.addFriend(2L, 4L);
        userStorage.addFriend(2L, 5L);

        List<UserEntity> friends = userStorage.getCommonFriends(1L, 2L);

        assertThat(friends)
                .hasSize(2)
                .containsAll(List.of(new UserEntity(3L, "ivan@yandex.ru", "ivan", "User_ivan",
                                LocalDate.of(1992, 2, 2)),
                        new UserEntity(5L, "drani@yandex.ru", "drani", "User_drani",
                                LocalDate.of(2000, 11, 11))));//скопировать из sql id 3 / 5
    }
}
