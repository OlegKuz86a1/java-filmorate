package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        User friend = userStorage.findById(friendId).orElseThrow(() ->new NotFoundException("Друг не найден"));
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public Set<Long> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    public Set<Long> getCommonFriends(Long userId, Long otherUserId) {
        Set<Long> userFriends = userStorage.getFriends(userId);
        Set<Long> otherUserFriends = userStorage.getFriends(otherUserId);
        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toSet());
    }
}
