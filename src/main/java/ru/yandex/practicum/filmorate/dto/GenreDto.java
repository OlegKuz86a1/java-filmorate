package ru.yandex.practicum.filmorate.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GenreDto {
    private Integer id;
    private String name;
}
