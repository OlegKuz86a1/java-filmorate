package ru.yandex.practicum.filmorate.assistants;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CorrectBirthdayDeserialized extends JsonDeserializer<LocalDate> {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String birthDateStr = jsonParser.getText();
        try {
            LocalDate birthDate = LocalDate.parse(birthDateStr, formatter);
            if (birthDate.isAfter(LocalDate.now())) {
                throw new ValidationException("дата рождения не может быть в будущем.");
            }
            return birthDate;
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("невеный вормат даты", e);
        }
    }
}
