package com.skillbox.vacancy.tracker.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class MainMenuCommand implements BotCommand {
    @Override
    public boolean isApply(String message) {
        return message.startsWith("/menu");
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        return null;
    }
}
