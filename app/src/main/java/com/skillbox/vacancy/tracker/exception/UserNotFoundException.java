package com.skillbox.vacancy.tracker.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String MSG = "Не найден пользователь с id = %d";

    public UserNotFoundException(Long userId) {
        super(MSG.formatted(userId));
    }
}
