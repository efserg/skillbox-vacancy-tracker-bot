package com.skillbox.vacancy.tracker.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Задача нотификации пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationTask implements Serializable, BotTask {

    /**
     * Идентификатор пользователя в Telegram
     */
    Long userId;

    /**
     * Id чата, куда бот будет присылать обновления
     */
    Long chatId;

    /**
     * Время нотификации
     */
    LocalTime notificationTime;

}
