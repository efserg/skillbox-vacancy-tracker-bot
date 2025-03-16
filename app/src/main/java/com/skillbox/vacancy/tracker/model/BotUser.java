package com.skillbox.vacancy.tracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Пользователь бота
 */
@Data
@NoArgsConstructor
public class BotUser implements Serializable {
    /**
     * Уникальный идентификатор пользователя в Telegram
     */
    Long id;

    /**
     * Имя пользователя
     */
    String firstName;

    /**
     * Фамилия пользователя (если указана)
     */
    String lastName;

    /**
     * Username пользователя (если указан, например, @username)
     */
    String username;

    /**
     * Смещение часового пояса пользователя от UTC
     */
    int offsetSeconds;

    /**
     * Список чатов, в которых пользователь подключил бота
     */
    List<Long> chats;

    public BotUser(Long id, String firstName, String lastName, String username, Long chatId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.offsetSeconds = 0; // UTC
        addChat(chatId);
    }

    public void addChat(Long chatId) {
        if (this.chats == null) {
            this.chats = new ArrayList<>();
        }
        chats.add(chatId);
    }
}
