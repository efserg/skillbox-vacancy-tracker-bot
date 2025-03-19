package com.skillbox.vacancy.tracker.bot;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Value;

@Value
public class TelegramUpdateInfo {
    String message;
    Long chatId;
    Long userId;
    Integer messageId;
    String firstName;
    String lastName;
    String userName;
    String command;
    List<String> params;

    public boolean hasParams() {
        return !params.isEmpty();
    }

    public String joinParams() {
        return params.stream().collect(Collectors.joining(" "));
    }
}
