package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.filmorate.assistants.LimitedLengthDeserializer;
import ru.yandex.practicum.filmorate.assistants.PositiveNumberDeserializer;
import ru.yandex.practicum.filmorate.assistants.ReleaseDateValidatorDeserializer;

import java.time.LocalDate;


@Data
@NoArgsConstructor
public class Film {
    private Long id;
    private String name;
    @JsonDeserialize(using = LimitedLengthDeserializer.class, contentUsing = LimitedLengthDeserializer.class)
    private String description;
    @JsonDeserialize(using = ReleaseDateValidatorDeserializer.class)
    private LocalDate releaseDate;
    @JsonDeserialize(using = PositiveNumberDeserializer.class)
    private int duration;
}
