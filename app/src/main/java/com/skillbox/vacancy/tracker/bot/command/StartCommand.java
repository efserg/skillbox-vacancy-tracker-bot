package com.skillbox.vacancy.tracker.bot.command;

import com.skillbox.vacancy.tracker.bot.TelegramUpdateInfo;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import com.skillbox.vacancy.tracker.model.BotUser;
import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import com.skillbox.vacancy.tracker.service.FindVacancyTaskService;
import com.skillbox.vacancy.tracker.service.UserService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@RequiredArgsConstructor
public class StartCommand implements BotCommand {

    private final UserService userService;

    private final FindVacancyTaskService findVacancyTaskService;

    @Override
    public boolean isApply(String message) {
        return message.startsWith("/start");
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        String msg;
        final Long userId = update.getUserId();
        final Long chatId = update.getChatId();
        if (!userService.exist(userId)) {
            final BotUser user = new BotUser(userId, update.getFirstName(), update.getLastName(), update.getUserName(),
                    chatId);
            userService.save(user);
            msg = """
                    Вы успешно зарегистрированы в Vacancy Tracker Bot\\!
                    Будет использован часовой пояс UTC, для изменения введите часовой пояс в формате `UTC+3:30`\\.
                    Используйте команду `/menu` для получения списка команд\\.
                    """;
        } else {
            final BotUser user = userService.find(userId);
            final int offsetSeconds = user.getOffsetSeconds();
            ZoneOffset offset = ZoneOffset.ofTotalSeconds(offsetSeconds);
            final String zoneId = ZoneId.ofOffset("UTC", offset).toString();
            String filters = "Ни один фильтр пока не задан\\.";
            if (findVacancyTaskService.exists(userId, chatId)) {
                final FindVacancyTask findVacancyTask = findVacancyTaskService.find(userId, chatId);
                filters = "\n*Ранее заданные фильтры:*\n%s\n".formatted(findVacancyTask);

            }
            msg = """
                    Vacancy Tracker Bot приветствует вас\\!
                    Вы ранее регистрировались в нашем боте, можете приступать к его использованию\\.
                    Сейчас используется часовой пояс %s, для изменения введите, например `UTC+3:30`
                    %s
                    Введите команду `/menu` для получения списка команд\\.
                    """.formatted(zoneId, filters);
        }

        final InlineKeyboardButton menuBtn = InlineKeyboardButton.builder()
                .text("Menu").callbackData("/menu").build();

        return SendMessage.builder()
                .chatId(chatId)
                .parseMode("MarkdownV2")
                .replyMarkup(new InlineKeyboardMarkup(List.of(new InlineKeyboardRow(menuBtn))))
                .text(msg)
                .build();
    }
}
