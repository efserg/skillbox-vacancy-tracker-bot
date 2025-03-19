package com.skillbox.vacancy.tracker.bot;

import java.util.List;
import java.util.stream.Collectors;
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
        final Integer messageId;

        if (update.hasCallbackQuery()) {
            final CallbackQuery callbackQuery = update.getCallbackQuery();
            msg = callbackQuery.getData();
            chatId = callbackQuery.getMessage().getChatId();
            messageId = callbackQuery.getMessage().getMessageId();
            user = callbackQuery.getFrom();

        } else {
            final Message message = update.getMessage();
            chatId = message.getChatId();
            msg = message.getText();
            user = message.getFrom();
            messageId = message.getMessageId();
        }

        final Long id = user.getId();
        final String firstName = user.getFirstName();
        final String lastName = user.getLastName();
        final String userName = user.getUserName();

        final List<String> tokens = (msg == null || msg.trim().isEmpty())
                ? List.of() : List.of(msg.trim().split("\\s+"));
        final String command = tokens.stream().findFirst().orElse(null);
        List<String> params = tokens.stream()
                .skip(1)
                .collect(Collectors.toList());
        return new TelegramUpdateInfo(msg, chatId, id, messageId,
                firstName, lastName, userName, command, params);
    }
}
