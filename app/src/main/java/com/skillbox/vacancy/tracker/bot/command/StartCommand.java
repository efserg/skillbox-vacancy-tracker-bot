package com.skillbox.vacancy.tracker.bot.command;

import com.skillbox.vacancy.tracker.bot.TelegramUpdateInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.skillbox.vacancy.tracker.model.BotUser;
import com.skillbox.vacancy.tracker.service.UserService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

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
            final BotUser user = new BotUser(update.getUserId(), update.getFirstName(), update.getLastName(), update.getUserName(),
                    update.getChatId());
            userService.save(user);
            msg = """
                    Вы успешно зарегистрированы в Vacancy Tracker Bot\\!
                    Будет использован часовой пояс UTC, для изменения введите часовой пояс в формате `UTC+3`\\.
                    Используйте команду `/menu` для получения списка команд\\.
                    """;
        } else {
            msg = """
                    Vacancy Tracker Bot приветствует вас\\!
                    Вы ранее регистрировались в нашем боте, можете приступать к его использованию\\.
                    Введите команду `/menu` для получения списка команд\\.
                    """;
        }

        final InlineKeyboardButton menuButton = new InlineKeyboardButton("Menu");
        menuButton.setCallbackData("/menu");

        return SendMessage.builder()
                .chatId(update.getChatId())
                .parseMode("MarkdownV2")
                .replyMarkup(new InlineKeyboardMarkup(List.of(new InlineKeyboardRow(menuButton))))
                .text(msg)
                .build();
    }
}
