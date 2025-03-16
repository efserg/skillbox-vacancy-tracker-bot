package com.skillbox.vacancy.tracker.bot.command;

import com.skillbox.vacancy.tracker.bot.TelegramUpdateInfo;
import com.skillbox.vacancy.tracker.model.BotUser;
import com.skillbox.vacancy.tracker.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@RequiredArgsConstructor
public class UnknownCommand implements BotCommand {

    @Override
    public boolean isApply(String message) {
        return false;
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        return SendMessage.builder()
                .chatId(update.getChatId())
                .text("Неизвестная команда!")
                .build();
    }
}
