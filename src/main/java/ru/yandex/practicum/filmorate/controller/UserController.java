package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserBaseDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> allUsers() {
        log.info("возвращаем список пользователей");
        return userService.allUsers();
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserBaseDto userBaseDto) {
        log.info("запрос на создание нового пользователя: {}", userBaseDto);
        return userService.create(userBaseDto);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UserDto userDto) {
        log.info("запрос на обновление пользователя: {}", userDto.getName());
        return userService.update(userDto);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable("userId") Long userId, @PathVariable("friendId") Long friendId) {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<UserDto> getFriends(@PathVariable Long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public List<UserDto> getCommonFriends(@PathVariable("userId") Long userId,
                                             @PathVariable("otherUserId") Long otherUserId) {
      return userService.getCommonFriends(userId, otherUserId);
    }
}

