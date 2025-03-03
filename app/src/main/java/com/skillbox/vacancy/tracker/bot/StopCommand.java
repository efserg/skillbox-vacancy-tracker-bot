package com.skillbox.vacancy.tracker.bot;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.skillbox.vacancy.tracker.service.UserService;

@RequiredArgsConstructor
public class StopCommand implements BotCommand {

    private final UserService userService;

    @Override
    public boolean isApply(String message) {
        return message.startsWith("/stop");
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        userService.delete(update.getUserId());
        return SendMessage.builder()
                .chatId(update.getChatId())
                .text("""
                        Вся информация, связанная с вашими данными, удалена!
                        Благодарим за использование Vacancy Tracker Bot!
                        """)
                .build();
    }
}
