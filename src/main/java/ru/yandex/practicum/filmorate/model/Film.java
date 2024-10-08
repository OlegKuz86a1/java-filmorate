package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long countLikes;
    private AgeRating rating;
    private Genre genre;
}
