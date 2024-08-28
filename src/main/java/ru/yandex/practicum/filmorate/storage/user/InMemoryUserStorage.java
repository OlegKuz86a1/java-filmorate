package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.UserEntity;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, UserEntity> users = new HashMap<>();
    private final Map<Long, Set<Long>> friendships = new HashMap<>();

    @Override
    public List<UserEntity> allUsers() {
        return (List<UserEntity>) users.values();
    }

    @Override
    public UserEntity create(UserEntity userEntity) {
        validateUsers(userEntity);
        userEntity.setId(getNextId());
        users.put(userEntity.getId(), userEntity);
        log.info("пользователь успешно создан: {}", userEntity);
        return userEntity;
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        if (userEntity.getId() == null) {
            throw new ValidationException("Id должен быть");
        }
        validateUsers(userEntity);

        if (!users.containsKey(userEntity.getId())) {
            log.warn("неудалось найти пользователя с таким id: {}", userEntity.getName());
            throw new NotFoundException("Не найден пользователь");
        }
        users.put(userEntity.getId(), userEntity);
        log.info("пользователь успешно обновлен: {}", userEntity.getName());
        return userEntity;
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
    public List<UserEntity> getFriends(Long userId) {
        Set<Long> friends = friendships.getOrDefault(userId, Collections.emptySet());
        log.info("Другом пользователя {} является {}", userId, friends);
        return friends.stream()
                .map(users::get)
                .toList();
    }

    @Override
    public Optional<UserEntity> findById(Long userId) {
        if (userId == null) {
            throw new ValidationException("Ожидается id пользователя");
        }
        return users.entrySet().stream()
                .filter(entry -> entry.getKey().equals(userId))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @Override
    public List<UserEntity> getCommonFriends(Long userId, Long otherUserId) {
        List<UserEntity> userEntityFriends = getFriends(userId);
        List<UserEntity> otherUserEntityFriends = getFriends(otherUserId);
        return userEntityFriends.stream()
                .filter(otherUserEntityFriends::contains)
                .collect(Collectors.toList());
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

    private void validateUsers(UserEntity userEntity) {
        if (userEntity.getLogin() == null || userEntity.getLogin().contains(" ") || userEntity.getLogin().isBlank()) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (userEntity.getEmail() == null || !userEntity.getEmail().contains("@") || userEntity.getEmail().isBlank()) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @;");
        }
        if (userEntity.getName() == null || userEntity.getName().isBlank()) {
            userEntity.setName(userEntity.getLogin());
        }
    }
}
