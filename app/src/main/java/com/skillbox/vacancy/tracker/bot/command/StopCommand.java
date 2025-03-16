package com.skillbox.vacancy.tracker.bot.command;

import com.skillbox.vacancy.tracker.bot.TelegramUpdateInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.skillbox.vacancy.tracker.service.UserService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@RequiredArgsConstructor
public class StopCommand implements BotCommand {

    private final UserService userService;

    @Override
    public boolean isApply(String message) {
        return message.startsWith("/stop");
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        final String[] tokens = update.getMessage().split("\\s+");
        if (tokens.length < 2) {
            return SendMessage.builder()
                    .chatId(update.getChatId())
                    .replyMarkup(getKeyboardMarkup())
                    .text("""
                            Вся информация, связанная с вашими данными, будет удалена!
                            Вы уверены?
                            """)
                    .build();
        } else {
            final String answer = tokens[1];
            if ("YES".equals(answer)) {
                userService.delete(update.getUserId());
                return SendMessage.builder()
                        .chatId(update.getChatId())
                        .text("""
                                Вся информация, связанная с вашими данными, удалена!
                                Благодарим за использование Vacancy Tracker Bot!
                                """)
                        .build();
            }
            return SendMessage.builder()
                    .chatId(update.getChatId())
                    .text("""
                            Благодарим, что продолжаете пользоваться Vacancy Tracker Bot!
                            """)
                    .build();
        }
    }

    @NotNull
    private static InlineKeyboardMarkup getKeyboardMarkup() {
        final InlineKeyboardButton yesButton = InlineKeyboardButton.builder()
                .text("Да")
                .callbackData("/stop YES")
                .build();
        final InlineKeyboardButton noButton = InlineKeyboardButton.builder()
                .text("Нет")
                .callbackData("/stop no")
                .build();
        return new InlineKeyboardMarkup(List.of(new InlineKeyboardRow(yesButton, noButton)));
    }
}
