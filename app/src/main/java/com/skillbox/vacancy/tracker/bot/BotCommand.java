package com.skillbox.vacancy.tracker.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface BotCommand {
    boolean isApply(String message);

    SendMessage execute(TelegramUpdateInfo update);

}
