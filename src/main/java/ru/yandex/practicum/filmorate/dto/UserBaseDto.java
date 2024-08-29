package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.assistants.CorrectBirthdayDeserialized;

import java.time.LocalDate;

@SuperBuilder
@Getter
@Setter
@Data
@NoArgsConstructor
public class UserBaseDto {

    @Email
    private String email;
    private String login;
    @NotNull
    private String name;
    @JsonDeserialize(using = CorrectBirthdayDeserialized.class)
    private LocalDate birthday;
}
