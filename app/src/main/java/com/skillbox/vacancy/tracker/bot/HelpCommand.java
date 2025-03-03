package com.skillbox.vacancy.tracker.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class HelpCommand implements BotCommand {
    @Override
    public boolean isApply(String message) {
        return message.startsWith("/help");
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        String msg = """
                    Добро пожаловать в Vacancy Tracker Bot!
                    Сейчас используется чaсовой пояс UTC, для изменения введите часовой пояс в формате `UTC+3`.
                    Также вы можете добавить новое задание на поиск вакансий - используйте команду `/menu` для вызова основного меню
                    """;

        return SendMessage.builder()
                .chatId(update.getChatId())
                .parseMode("MarkdownV2")
                .text(msg)
                .build();
    }
}
