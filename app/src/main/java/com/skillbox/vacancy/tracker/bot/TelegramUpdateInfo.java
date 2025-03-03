package com.skillbox.vacancy.tracker.bot;

import lombok.Value;

@Value
public class TelegramUpdateInfo {
    String message;
    Long chatId;
    Long userId;
    String firstName;
    String lastName;
    String userName;
}
