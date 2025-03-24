package com.skillbox.vacancy.tracker.bot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.skillbox.vacancy.tracker.bot.TelegramUpdateInfo;

public interface BotCommand {
    boolean isApply(String message);

    SendMessage execute(TelegramUpdateInfo update);

}
