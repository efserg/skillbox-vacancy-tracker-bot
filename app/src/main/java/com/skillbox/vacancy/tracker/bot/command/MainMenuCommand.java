package com.skillbox.vacancy.tracker.bot.command;

import com.skillbox.vacancy.tracker.bot.TelegramUpdateInfo;
import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import com.skillbox.vacancy.tracker.model.NotificationTask;
import com.skillbox.vacancy.tracker.service.FindVacancyTaskService;
import com.skillbox.vacancy.tracker.service.NotificationTaskService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@RequiredArgsConstructor
public class MainMenuCommand implements BotCommand {

    private final FindVacancyTaskService findVacancyTaskService;
    private final NotificationTaskService notificationTaskService;

    @Override
    public boolean isApply(String message) {
        return message.startsWith("/menu");
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        final Long userId = update.getUserId();
        final Long chatId = update.getChatId();

        final FindVacancyTask findVacancyTask;
        final NotificationTask notificationTask;

        if (findVacancyTaskService.exists(userId, chatId)) {
            findVacancyTask = findVacancyTaskService.find(userId, chatId);
        } else {
            findVacancyTask = FindVacancyTask.builder()
                    .userId(userId)
                    .chatId(chatId)
                    .build();
            findVacancyTaskService.save(findVacancyTask);
        }

        if (notificationTaskService.exists(userId, chatId)) {
            notificationTask = notificationTaskService.find(userId, chatId);
        } else {
            notificationTask = NotificationTask.builder()
                    .userId(userId)
                    .chatId(chatId)
                    .build();
            notificationTaskService.save(notificationTask);
        }

        return SendMessage.builder()
                .chatId(chatId)
                .parseMode("MarkdownV2")
                .replyMarkup(getKeyboardMarkup(findVacancyTask, notificationTask))
                .text("""
                        Задайте критерии поиска и время нотификации\\.
                        Затем нажмите "Готово", либо введите команду `/ready`\\.
                       """)
                .build();
    }

    @NotNull
    private InlineKeyboardMarkup getKeyboardMarkup(FindVacancyTask task, NotificationTask notificationTask) {
        final InlineKeyboardButton regionBtn = InlineKeyboardButton.builder()
                .text("Регион" + getSetValue(task.getRegion()))
                .callbackData("/region")
                .build();
        final InlineKeyboardButton experienceBtn = InlineKeyboardButton.builder()
                .text("Минимальный опыт" + getSetValue(task.getExperienceFrom()))
                .callbackData("/experience")
                .build();
        final InlineKeyboardButton salaryBtn = InlineKeyboardButton.builder()
                .text("Минимальная зарплата" + getSetValue(task.getSalaryMin()))
                .callbackData("/salary")
                .build();
        final InlineKeyboardButton keywordBtn = InlineKeyboardButton.builder()
                .text("Слово для поиска" + getSetValue(task.getKeyword()))
                .callbackData("/keyword")
                .build();
        final InlineKeyboardButton notificationBtn = InlineKeyboardButton.builder()
                .text("Настройки нотификации" + getSetValue(notificationTask.getNotificationLocalTime()))
                .callbackData("/notify")
                .build();
        final InlineKeyboardButton readyBtn = InlineKeyboardButton.builder()
                .text("Готово")
                .callbackData("/ready")
                .build();
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(regionBtn))
                .keyboardRow(new InlineKeyboardRow(experienceBtn))
                .keyboardRow(new InlineKeyboardRow(salaryBtn))
                .keyboardRow(new InlineKeyboardRow(keywordBtn))
                .keyboardRow(new InlineKeyboardRow(notificationBtn))
                .keyboardRow(new InlineKeyboardRow(readyBtn))
                .build();
    }

    @NotNull
    private static String getSetValue(Object value) {
        return value == null ? "" : (" [" + value + "]");
    }
}
