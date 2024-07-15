package ru.yandex.practicum.filmorate.assistants;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.io.IOException;

public class PositiveNumberDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();
        try {
            int duration = Integer.parseInt(text);
            if (duration <= 0) {
                throw new ValidationException("продолжительность фильма должна быть положительным числом.");
            }
            return duration;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("неверный формат числа продолжительности", e);
        }
    }
}
