package com.skillbox.vacancy.tracker.bot;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.skillbox.vacancy.tracker.repository.model.BotUser;
import com.skillbox.vacancy.tracker.service.UserService;

@RequiredArgsConstructor
public class StartCommand implements BotCommand {

    private final UserService userService;

    @Override
    public boolean isApply(String message) {
        return message.startsWith("/start");
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        String msg;
        if (!userService.exist(update.getUserId())) {
            final BotUser user = new BotUser(update.getUserId(), update.getFirstName(), update.getLastName(), update.getUserName(), update.getChatId());
            userService.save(user);
            msg = """
                    Вы успешно зарегистрированы в Vacancy Tracker Bot!
                    Будет использован часовой пояс UTC, для изменения введите часовой пояс в формате `UTC+3`.
                    Используйте команду /help для получения справочной информации.
                    """;
        } else {
            msg = """
                    Vacancy Tracker Bot приветствует вас!
                    Вы ранее регистрировались в нашем боте, можете приступать к его использованию.
                    Введите команду /help для получения справочной информации.
                    """;
        }

        return SendMessage.builder()
                .chatId(update.getChatId())
                .parseMode("MarkdownV2")
                .text(msg)
                .build();
    }
}
