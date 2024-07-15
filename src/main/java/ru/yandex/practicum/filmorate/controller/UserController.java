package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> allUsers() {
        log.info("возвращаем список пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("запрос на создание нового пользователя: {}", user);
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }

        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @;");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("пользователь успешно создан: {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("запрос на обновление пользователя: {}", user.getName());
        if (user.getId() == null) {
            throw new ValidationException("Id должен быть");
        }

        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }

        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @;");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("пользователь успешно обновлен: {}", user.getName());
        } else  {
            log.warn("неудалось найти пользователя с таким id: {}", user.getName());
            throw new NotFoundException("Не найден пользователь");
        }

        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Генерация нового ID для пользователя: текущий максимум - {}, новый ID - {}", currentMaxId, ++currentMaxId);
        return currentMaxId;
    }
}
