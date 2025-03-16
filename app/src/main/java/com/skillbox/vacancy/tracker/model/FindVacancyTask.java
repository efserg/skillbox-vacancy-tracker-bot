package com.skillbox.vacancy.tracker.model;

import java.io.Serializable;
import java.time.Instant;

import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

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
     * Максимальный опыт (годы)
     */
    Integer experienceTo;

    /**
     * Ключевое слово
     */
    String keyword;

    /**
     * Дата и время последнего успешного запроса на сервис вакансий
     */
    Instant updatedAt;

    /**
     * Статус последнего выполнения задачи
     */
    TaskResultStatus status;

    @Builder
    public FindVacancyTask(Long userId, Long region,
                           Integer salaryMin, Integer experienceFrom, Integer experienceTo,
                           String keyword) {
        this.userId = userId;
        this.region = region;
        this.salaryMin = salaryMin;
        this.experienceFrom = experienceFrom;
        this.experienceTo = experienceTo;
        this.keyword = keyword;
        this.status = TaskResultStatus.WAIT;
        this.updatedAt = null;
    }
}
