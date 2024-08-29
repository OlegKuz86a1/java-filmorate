package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MpaEntity;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {

    Optional<MpaEntity> getById(Integer id);

    List<MpaEntity> getAll();
}
