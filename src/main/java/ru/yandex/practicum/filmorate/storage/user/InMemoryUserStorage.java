package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> friendships = new HashMap<>();

    @Override
    public Collection<User> allUsers() {
        return users.values();
    }

    @Override
    public User create(User user) {
        validateUsers(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("пользователь успешно создан: {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id должен быть");
        }
        validateUsers(user);

        if (!users.containsKey(user.getId())) {
            log.warn("неудалось найти пользователя с таким id: {}", user.getName());
            throw new NotFoundException("Не найден пользователь");
        }
        users.put(user.getId(), user);
        log.info("пользователь успешно обновлен: {}", user.getName());
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        friendships.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        friendships.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        removeFriendByUserId(userId, friendId);
        removeFriendByUserId(friendId, userId);
    }

    @Override
    public Set<User> getFriends(Long userId) {
        Set<Long> friends = friendships.getOrDefault(userId, Collections.emptySet());
        log.info("Другом пользователя {} является {}", userId, friends);
        return friends.stream()
                .map(users::get)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<User> findById(Long userId) {
        if (userId == null) {
            throw new ValidationException("Ожидается id пользователя");
        }
        return users.entrySet().stream()
                .filter(entry -> entry.getKey().equals(userId))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    private void removeFriendByUserId(Long userId, Long friendId) {
        Set<Long> friends = friendships.getOrDefault(userId, Collections.emptySet());
        friends.remove(friendId);
        friendships.put(userId, friends);
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

    private void validateUsers(User user) {
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @;");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
