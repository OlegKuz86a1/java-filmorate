package ru.yandex.practicum.filmorate.assistants;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.io.IOException;

public class LimitedLengthDeserializer extends JsonDeserializer<String> {
    private final int maxLength = 100;

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();
        if (value.length() > maxLength) {
            throw new ValidationException("максимальная длина описания должна быть — 100 символов;");
        }
        return value;
    }
}
