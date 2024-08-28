package ru.yandex.practicum.filmorate.model;

import lombok.*;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@Data
public class UserEntity {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
