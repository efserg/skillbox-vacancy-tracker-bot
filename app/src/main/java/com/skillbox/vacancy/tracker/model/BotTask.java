package com.skillbox.vacancy.tracker.model;

public interface BotTask {

    /**
     * Идентификатор пользователя в Telegram
     */
    Long getUserId();

    /**
     * Id чата, куда бот будет присылать обновления
     */
    Long getChatId();
}
