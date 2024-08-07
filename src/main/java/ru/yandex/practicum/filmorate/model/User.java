package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.assistants.CorrectBirthdayDeserialized;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    @JsonDeserialize(using = CorrectBirthdayDeserialized.class)
    private LocalDate birthday;
    private boolean friendship;
}
