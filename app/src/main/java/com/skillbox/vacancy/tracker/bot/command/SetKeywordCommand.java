package com.skillbox.vacancy.tracker.bot.command;

import com.skillbox.vacancy.tracker.bot.BotCommandStorage;
import com.skillbox.vacancy.tracker.bot.TelegramUpdateInfo;
import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import com.skillbox.vacancy.tracker.service.FindVacancyTaskService;
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
public class SetKeywordCommand implements BotCommand {

    private static final String COMMAND_PREFIX = "/keyword";

    private final FindVacancyTaskService findVacancyTaskService;

    private final BotCommandStorage commandStorage;

    @Override
    public boolean isApply(String message) {
        return message.startsWith(COMMAND_PREFIX);
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        final SendMessageBuilder<?, ?> sendMessageBuilder = SendMessage.builder()
                .text("Выбирайте ключевое слово из предложенных вариантов или введите команду, например `/keyword программист kotlin`")
                .parseMode("MarkdownV2")
                .chatId(update.getChatId());
        if (!update.hasParams()) {
            return sendMessageBuilder.replyMarkup(getKeyboardMarkup()).build();
        }
        final Long chatId = update.getChatId();
        final Long userId = update.getUserId();
        String keywords = update.joinParams();
        if (keywords.isBlank()) {
            keywords = null;
        }
        if (findVacancyTaskService.exists(userId, chatId)) {
            final FindVacancyTask task = findVacancyTaskService.find(userId, chatId);
            task.setKeyword(keywords);
            findVacancyTaskService.save(task);
        } else {
            final FindVacancyTask task = FindVacancyTask.builder()
                    .keyword(keywords)
                    .userId(userId)
                    .chatId(chatId)
                    .build();
            findVacancyTaskService.save(task);
        }
        return commandStorage.get(MainMenuCommand.class).execute(update);
    }

    @NotNull
    private InlineKeyboardMarkup getKeyboardMarkup() {
        final List<InlineKeyboardRow> keyboard =
                getKeywords()
                        .map(r -> InlineKeyboardButton.builder()
                                .text(r)
                                .callbackData(COMMAND_PREFIX + " " + r)
                                .build())
                        .map(InlineKeyboardRow::new)
                        .collect(Collectors.toList());

        return new InlineKeyboardMarkup(keyboard);
    }

    private Stream<String> getKeywords() {
        return Stream.of(
                "Java", "Инженер-программист", "Стажировка Java",
                "Программист-стажер", "Developer", "Software engineer"
        );
    }
}
