package com.skillbox.vacancy.tracker.bot.command;

import com.skillbox.vacancy.tracker.bot.TelegramUpdateInfo;
import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import com.skillbox.vacancy.tracker.model.NotificationTask;
import com.skillbox.vacancy.tracker.service.scheduler.UserNotificator;
import com.skillbox.vacancy.tracker.service.scheduler.VacancyLoader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@RequiredArgsConstructor
public class ReadyCommand implements BotCommand {

    private static final String COMMAND_PREFIX = "/ready";

    private final UserNotificator userNotificator;

    private final VacancyLoader vacancyLoader;

    @Override
    public boolean isApply(String message) {
        return message.startsWith(COMMAND_PREFIX);
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        final Long chatId = update.getChatId();
        final Long userId = update.getUserId();

        final NotificationTask notificationTask = userNotificator.scheduleTask(userId, chatId);

        if (notificationTask == null) {
            final InlineKeyboardButton notificationBtn = InlineKeyboardButton.builder()
                    .text("Настройки нотификации")
                    .callbackData("/notify")
                    .build();

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Не задано время нотификации! Задайте его, нажав кнопку ниже")
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(new InlineKeyboardRow(notificationBtn)).build())
                    .build();
        }

        final FindVacancyTask findVacancyTask = vacancyLoader.scheduleTask(userId, chatId);

        return SendMessage.builder()
                .chatId(chatId)
                .parseMode("MarkdownV2")
                .replyMarkup(new InlineKeyboardMarkup(
                        List.of(
                                new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text("Menu")
                                                .callbackData("/menu")
                                                .build()))))
                .text("""
                        Запланировано выполнение задачи
                        нотификации о найденных вакансиях на `%s`\\.
                        Будут искаться новые вакансии по
                        следующим параметрам:
                        
                        %s""".formatted(notificationTask.getNotificationLocalTime(), findVacancyTask.toString())
                )
                .build();
    }
}
