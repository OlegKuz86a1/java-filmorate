package ru.yandex.practicum.filmorate.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Getter
@Setter
@Data
@NoArgsConstructor
public class UserDto extends UserBaseDto {

    private Long id;
}
