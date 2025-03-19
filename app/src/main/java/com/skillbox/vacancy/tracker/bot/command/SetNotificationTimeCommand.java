package com.skillbox.vacancy.tracker.bot.command;

import com.skillbox.vacancy.tracker.bot.BotCommandStorage;
import com.skillbox.vacancy.tracker.bot.TelegramUpdateInfo;
import com.skillbox.vacancy.tracker.model.NotificationTask;
import com.skillbox.vacancy.tracker.service.NotificationTaskService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@RequiredArgsConstructor
public class SetNotificationTimeCommand implements BotCommand {

    private static final String COMMAND_PREFIX = "/notify";

    private final NotificationTaskService notificationTaskService;

    private final BotCommandStorage commandStorage;

    @Override
    public boolean isApply(String message) {
        return message.startsWith(COMMAND_PREFIX);
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        final SendMessageBuilder<?, ?> sendMessageBuilder = SendMessage.builder()
                .text("Выбирайте время нотификации, либо введите команду, например `/notify 15 30` \\(разделитель часов и минут \\- пробел\\)")
                .parseMode("MarkdownV2")
                .chatId(update.getChatId());
        if (!update.hasParams()) {
            return sendMessageBuilder.replyMarkup(getKeyboardMarkup()).build();
        }
        final Long chatId = update.getChatId();
        final Long userId = update.getUserId();
        final int hours = Integer.parseInt(update.getParams().get(0));
        final int minutes = Integer.parseInt(update.getParams().get(1));
        final int notificationTime = hours * 60 * 60 + minutes * 60;
        if (notificationTaskService.exists(userId, chatId)) {
            final NotificationTask task = notificationTaskService.find(userId, chatId);
            task.setNotificationTime(notificationTime);
            notificationTaskService.save(task);
        } else {
            final NotificationTask task = NotificationTask.builder()
                    .notificationTime(notificationTime)
                    .userId(userId)
                    .chatId(chatId)
                    .build();
            notificationTaskService.save(task);
        }
        return commandStorage.get(MainMenuCommand.class).execute(update);
    }

    @NotNull
    private InlineKeyboardMarkup getKeyboardMarkup() {
        final List<InlineKeyboardRow> keyboard =
                getExperience()
                        .map(r -> InlineKeyboardButton.builder()
                                .text(r.description)
                                .callbackData(COMMAND_PREFIX + " " + r.hours + " " + r.minutes)
                                .build())
                        .map(InlineKeyboardRow::new)
                        .collect(Collectors.toList());

        return new InlineKeyboardMarkup(keyboard);
    }

    private Stream<NotificationTime> getExperience() {
        return Stream.of(
                new NotificationTime(12, 0, "12:00"),
                new NotificationTime(15, 0, "15:00"),
                new NotificationTime(17, 0, "17:00"),
                new NotificationTime(19, 0, "19:00"),
                new NotificationTime(21, 0, "21:00")
        );
    }

    private record NotificationTime(Integer hours, Integer minutes, String description) {

    }
}
