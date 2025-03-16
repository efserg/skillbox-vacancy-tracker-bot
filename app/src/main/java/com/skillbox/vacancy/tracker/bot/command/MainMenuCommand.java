package com.skillbox.vacancy.tracker.bot.command;

import com.skillbox.vacancy.tracker.bot.TelegramUpdateInfo;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

public class MainMenuCommand implements BotCommand {
    @Override
    public boolean isApply(String message) {
        return message.startsWith("/menu");
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        return SendMessage.builder()
                .chatId(update.getChatId())
                .replyMarkup(getKeyboardMarkup())
                .text("Выбирайте дальнейшее действие")
                .build();
    }

    @NotNull
    private static InlineKeyboardMarkup getKeyboardMarkup() {
        final InlineKeyboardButton regionBtn = InlineKeyboardButton.builder()
                .text("Регион")
                .callbackData("/region")
                .build();
        final InlineKeyboardButton experienceBtn = InlineKeyboardButton.builder()
                .text("Минимальный опыт")
                .callbackData("/experience")
                .build();
        final InlineKeyboardButton salaryBtn = InlineKeyboardButton.builder()
                .text("Минимальная зарплата")
                .callbackData("/salary")
                .build();
        final InlineKeyboardButton keywordBtn = InlineKeyboardButton.builder()
                .text("Слово для поиска")
                .callbackData("/keyword")
                .build();
        final InlineKeyboardButton notificationBtn = InlineKeyboardButton.builder()
                .text("Настройки нотификации")
                .callbackData("/notify")
                .build();
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(regionBtn))
                .keyboardRow(new InlineKeyboardRow(experienceBtn))
                .keyboardRow(new InlineKeyboardRow(salaryBtn))
                .keyboardRow(new InlineKeyboardRow(keywordBtn))
                .keyboardRow(new InlineKeyboardRow(notificationBtn))
                .build();


    }
}
