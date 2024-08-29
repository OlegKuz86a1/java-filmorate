package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class FilmDto extends FilmBaseDto {
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long countLikes;
}
