package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.assistants.PositiveNumberDeserializer;
import ru.yandex.practicum.filmorate.assistants.ReleaseDateValidatorDeserializer;

import java.time.LocalDate;
import java.util.List;

@SuperBuilder
@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class FilmBaseDto {
    @NotBlank
    private String name;
    @Length(max = 200)
    private String description;
    @JsonDeserialize(using = ReleaseDateValidatorDeserializer.class)
    private LocalDate releaseDate;
    @JsonDeserialize(using = PositiveNumberDeserializer.class)
    private Integer duration;
    private MpaDto mpa;
    private List<GenreDto> genres;
}
