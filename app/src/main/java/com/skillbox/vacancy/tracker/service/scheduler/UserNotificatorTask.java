package com.skillbox.vacancy.tracker.service.scheduler;

import com.skillbox.vacancy.tracker.model.Vacancy;
import com.skillbox.vacancy.tracker.service.VacancyService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@RequiredArgsConstructor
@Getter
public class UserNotificatorTask implements IdentifiableTask {

    private final Long userId;
    private final Long chatId;

    private final VacancyService vacancyService;

    private final TelegramClient telegramClient;

    @Override
    public void run() {

        final List<Vacancy> vacancies = vacancyService.find(userId, chatId);
        final String text = vacancies.stream()
                .sorted(Comparator.comparing(Vacancy::getSalaryMin).reversed())
                .limit(5)
                .map(vacancy -> ShortVacancy.builder()
                        .id(vacancy.getId())
                        .name(vacancy.getCompany().getName())
                        .minSalary(vacancy.getSalaryMin())
                        .maxSalary(vacancy.getSalaryMax())
                        .experience(vacancy.getRequirement().getExperience())
                        .url(vacancy.getVacUrl())
                        .build()
                )
                .map(ShortVacancy::toString)
                .collect(Collectors.joining("\n"));
        SendMessage chatMessage = SendMessage.builder()
                .chatId(chatId)
                .parseMode("MarkdownV2")
                .text(text)
                .build();

        try {
            telegramClient.execute(chatMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Builder
    @Value
    static class ShortVacancy {

        String id;
        String name;
        Integer minSalary;
        Integer maxSalary;
        String experience;
        String url;

        @Override
        public String toString() {
            return """
                    Компания: "`%s`"
                    Зарплата: %d-%d руб.
                    Опыт: %s руб.
                    Подробное описание вакансии *[здесь](%s)*
                    """.formatted(name, minSalary, maxSalary, experience, url);
        }
    }
}
