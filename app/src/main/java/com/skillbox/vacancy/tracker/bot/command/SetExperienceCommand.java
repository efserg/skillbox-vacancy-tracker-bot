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
public class SetExperienceCommand implements BotCommand {

    private static final String COMMAND_PREFIX = "/experience";

    private final FindVacancyTaskService findVacancyTaskService;

    private final BotCommandStorage commandStorage;

    @Override
    public boolean isApply(String message) {
        return message.startsWith(COMMAND_PREFIX);
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        final SendMessageBuilder<?, ?> sendMessageBuilder = SendMessage.builder()
                .text("Выбирайте минимальный опыт:")
                .chatId(update.getChatId());
        if (!update.hasParams()) {
            return sendMessageBuilder.replyMarkup(getKeyboardMarkup()).build();
        }
        final Long chatId = update.getChatId();
        final Long userId = update.getUserId();
        final String param = update.getParams().get(0);
        final Integer year = "null".equals(param) ? null : Integer.parseInt(param);
        if (findVacancyTaskService.exists(userId, chatId)) {
            final FindVacancyTask task = findVacancyTaskService.find(userId, chatId);
            task.setExperienceFrom(year);
            findVacancyTaskService.save(task);
        } else {
            final FindVacancyTask task = FindVacancyTask.builder()
                    .experienceFrom(year)
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
                getExperience()
                        .map(r -> InlineKeyboardButton.builder()
                                .text((r.year == null ? "" : (r.year + " "))
                                        + r.description)
                                .callbackData(COMMAND_PREFIX + " " + r.year)
                                .build())
                        .map(InlineKeyboardRow::new)
                        .collect(Collectors.toList());

        return new InlineKeyboardMarkup(keyboard);
    }

    private Stream<Experience> getExperience() {
        return Stream.of(
                new Experience(null, "Не учитывать"),
                new Experience(0, "Без опыта"),
                new Experience(1, "От года"),
                new Experience(3, "От трех лет"),
                new Experience(5, "От пяти лет")
        );
    }

    private record Experience(Integer year, String description) {

    }
}
