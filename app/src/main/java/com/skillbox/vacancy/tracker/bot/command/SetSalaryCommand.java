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
public class SetSalaryCommand implements BotCommand {

    private static final String COMMAND_PREFIX = "/salary";

    private final FindVacancyTaskService findVacancyTaskService;

    private final BotCommandStorage commandStorage;

    @Override
    public boolean isApply(String message) {
        return message.startsWith(COMMAND_PREFIX);
    }

    @Override
    public SendMessage execute(TelegramUpdateInfo update) {
        final SendMessageBuilder<?, ?> sendMessageBuilder = SendMessage.builder()
                .text("Выбирайте желаемую минимальную зарплату из предложенных вариантов или введите команду, например `/salary 10000`")
                .parseMode("MarkdownV2")
                .chatId(update.getChatId());
        if (!update.hasParams()) {
            return sendMessageBuilder.replyMarkup(getKeyboardMarkup()).build();
        }
        final Long chatId = update.getChatId();
        final Long userId = update.getUserId();
        final String param = update.getParams().get(0);
        final Integer salary = "null".equals(param) ? null : Integer.parseInt(param);
        if (findVacancyTaskService.exists(userId, chatId)) {
            final FindVacancyTask task = findVacancyTaskService.find(userId, chatId);
            task.setSalaryMin(salary);
            findVacancyTaskService.save(task);
        } else {
            final FindVacancyTask task = FindVacancyTask.builder()
                    .salaryMin(salary)
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
                getSalary()
                        .map(r -> InlineKeyboardButton.builder()
                                .text((r.salary == null ? "" : (r.salary + " "))
                                        + r.description)
                                .callbackData(COMMAND_PREFIX + " " + r.salary)
                                .build())
                        .map(InlineKeyboardRow::new)
                        .collect(Collectors.toList());

        return new InlineKeyboardMarkup(keyboard);
    }

    private Stream<Salary> getSalary() {
        return Stream.of(
                new Salary(null, "Не учитывать"),
                new Salary(25000, "От 25 тыс. руб"),
                new Salary(50000, "От 50 тыс. руб"),
                new Salary(100000, "От 100 тыс. руб."),
                new Salary(200000, "От 200 тыс. руб.")
        );
    }

    private record Salary(Integer salary, String description) {

    }
}
