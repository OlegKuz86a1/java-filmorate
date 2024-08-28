package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dto.UserBaseDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    public List<UserDto> allUsers() {
        return userMapper.toDto(userStorage.allUsers());
    }

    public UserDto create(@Valid @RequestBody UserBaseDto userBaseDto) {
        return userMapper.toDto(userStorage.create(userMapper.toEntity(userBaseDto)));
    }

    public UserDto update(UserDto userDto) {
        userStorage.findById(userDto.getId()).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return userMapper.toDto(userStorage.update(userMapper.toEntity(userDto)));
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userStorage.findById(friendId).orElseThrow(() -> new NotFoundException("Друг не найден"));
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userStorage.findById(friendId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userStorage.removeFriend(userId, friendId);
    }

    public List<UserDto> getFriends(Long userId) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return userMapper.toDto(userStorage.getFriends(userId));
    }

    public List<UserDto> getCommonFriends(Long userId, Long otherUserId) {
        return userMapper.toDto(userStorage.getCommonFriends(userId, otherUserId));
    }
}
