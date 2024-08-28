package ru.yandex.practicum.filmorate.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class MpaDto {
    private Integer id;
    private String name;
}
