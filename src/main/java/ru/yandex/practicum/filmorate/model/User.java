package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import ru.yandex.practicum.filmorate.assistants.CorrectBirthdayDeserialized;
import java.time.LocalDate;

@Data
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    @JsonDeserialize(using = CorrectBirthdayDeserialized.class)
    private LocalDate birthday;
}
