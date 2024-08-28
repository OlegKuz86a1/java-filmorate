package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Builder
@Getter
@Setter
@Data
public class GenreEntity {
    private Integer id;
    private String name;
}
