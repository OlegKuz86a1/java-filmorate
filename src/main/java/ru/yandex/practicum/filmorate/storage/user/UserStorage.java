package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<UserEntity> allUsers();

    UserEntity create(UserEntity userEntity);

    UserEntity update(UserEntity userEntity);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<UserEntity> getFriends(Long userId);

    Optional<UserEntity> findById(Long userId);

    List<UserEntity> getCommonFriends(Long userId, Long otherUserId);
}
