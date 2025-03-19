package com.skillbox.vacancy.tracker.model;

import java.io.Serializable;
import java.time.Instant;

import java.time.LocalTime;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import static java.util.Optional.ofNullable;

/**
 * Задача поиска вакансий
 */
@Data
@NoArgsConstructor
public class FindVacancyTask implements Serializable, BotTask {

    /**
     * Идентификатор пользователя в Telegram
     */
    Long userId;

    /**
     * Id чата, куда бот будет присылать обновления
     */
    Long chatId;

    /**
     * Код региона
     */
    Long region;

    /**
     * Минимальная зарплата
     */
    Integer salaryMin;

    /**
     * Минимальный опыт (годы)
     */
    Integer experienceFrom;

    /**
     * Ключевое слово
     */
    String keyword;

    /**
     * Дата и время последнего успешного запроса на сервис вакансий, в секундах от 1/1/1970
     */
    Long updatedAt;

    /**
     * Статус последнего выполнения задачи
     */
    TaskResultStatus status;

    @Builder
    public FindVacancyTask(Long userId, Long chatId, Long region,
                           Integer salaryMin, Integer experienceFrom,
                           String keyword) {
        this.userId = userId;
        this.chatId = chatId;
        this.region = region;
        this.salaryMin = salaryMin;
        this.experienceFrom = experienceFrom;
        this.keyword = keyword;
        this.status = TaskResultStatus.WAIT;
        this.updatedAt = null;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");

        ofNullable(region).ifPresent(value -> joiner.add("Код региона: `" + value + "`"));
        ofNullable(salaryMin).ifPresent(value -> joiner.add("Минимальная зарплата: `" + value + "`"));
        ofNullable(experienceFrom).ifPresent(value -> joiner.add("Минимальный опыт: `" + value + " лет`"));
        ofNullable(keyword)
                .filter(value -> !value.isEmpty())
                .ifPresent(value -> joiner.add("Ключевые слова: `" + value + "`"));

        return joiner.toString().isEmpty() ? "Фильтры не заданы" : joiner.toString();
    }
}
