package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class MpaEntity {
    private Integer id;
    private String name;
}
