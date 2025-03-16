package com.skillbox.vacancy.tracker.bot;

import com.skillbox.vacancy.tracker.bot.command.UnknownCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotCommandExecutor {

    private final BotCommandStorage commandStorage;
    private final UpdateMessageMapper messageMapper;

    public BotCommandExecutor(BotCommandStorage commandStorage) {
        this.commandStorage = commandStorage;
        this.messageMapper = new UpdateMessageMapper();
    }

    public SendMessage execute(Update update) {
        final TelegramUpdateInfo updateInfo = messageMapper.map(update);
        final String message = updateInfo.getMessage();
        if (message == null || message.trim().isEmpty()) {
            return SendMessage.builder()
                    .text("Введите команду")
                    .chatId(updateInfo.getChatId())
                    .build();
        }
        return commandStorage.getCommandList().stream()
                .filter(botCommand -> botCommand.isApply(message))
                .findFirst()
                .orElse(commandStorage.get(UnknownCommand.class))
                .execute(updateInfo);
    }
}
