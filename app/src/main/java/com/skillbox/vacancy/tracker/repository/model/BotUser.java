package com.skillbox.vacancy.tracker.repository.model;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
     * Id чата, куда бот будет присылать обновления
     */
    Long chatId;

    /**
     * Часовой пояс пользователя
     */
    ZoneId zone;

    /**
     * Задания пользователя поиска вакансий для бота
     */
    List<UpdateTask> tasks;

    public BotUser(Long id, String firstName, String lastName, String username, Long chatId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.chatId = chatId;
        this.zone = ZoneOffset.UTC;
        this.tasks = List.of();
    }
}
