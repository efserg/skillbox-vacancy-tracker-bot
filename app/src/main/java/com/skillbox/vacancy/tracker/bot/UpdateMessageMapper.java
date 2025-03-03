package com.skillbox.vacancy.tracker.bot;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * Извлекает информацию о команде и пользователе из сообщения Телеграм
 */
public class UpdateMessageMapper {
    TelegramUpdateInfo map(Update update) {
        final String msg;
        final Long chatId;
        final User user;

        if (update.hasCallbackQuery()) {
            final CallbackQuery callbackQuery = update.getCallbackQuery();
            msg = callbackQuery.getData();
            chatId = callbackQuery.getMessage().getChatId();
            user = callbackQuery.getFrom();
        } else {
            final Message message = update.getMessage();
            chatId = message.getChatId();
            msg = message.getText();
            user = message.getFrom();
        }

        final Long id = user.getId();
        final String firstName = user.getFirstName();
        final String lastName = user.getLastName();
        final String userName = user.getUserName();

        return new TelegramUpdateInfo(msg, chatId, id, firstName, lastName, userName);
    }
}
