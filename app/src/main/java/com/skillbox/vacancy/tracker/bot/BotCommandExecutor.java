package com.skillbox.vacancy.tracker.bot;

import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotCommandExecutor {
    private final List<BotCommand> commands;

    private final UpdateMessageMapper messageMapper;

    public BotCommandExecutor(List<BotCommand> commands) {
        this.commands = commands;
        messageMapper = new UpdateMessageMapper();
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
        return commands.stream()
                .filter(botCommand -> botCommand.isApply(message))
                .findFirst()
                .orElse(new HelpCommand())
                .execute(updateInfo);
    }
}
