package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Builder
@Getter
@Setter
@Data
@AllArgsConstructor
public class GenreEntity {
    private Integer id;
    private String name;
}
