package ru.yandex.practicum.filmorate.assistants;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReleaseDateValidatorDeserializer extends JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final LocalDate minReleaseDate = LocalDate.parse("1895-12-28", formatter);

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();
        System.out.println("text "+ text);
        try {
            LocalDate releaseDate = LocalDate.parse(text, formatter);
            if(releaseDate.isBefore(minReleaseDate)) {
                throw new ValidationException("дата релиза должна быть — не раньше 28 декабря 1895 года;");
            }
            return releaseDate;
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("неверный формат даты", e);
        }
    }
}
