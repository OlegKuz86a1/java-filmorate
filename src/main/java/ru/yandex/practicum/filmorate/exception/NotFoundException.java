package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private boolean isBadRequest;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, boolean isBadRequest) {
        super(message);
        this.isBadRequest = isBadRequest;
    }
}
