package com.skillbox.vacancy.tracker.repository.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Задача для бота
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UpdateTask implements Serializable {

    /**
     * Код региона
     */
    Long region;

    /**
     * Минимальная зарплата
     */
    BigDecimal minSalary;

    /**
     * Максимальная зарплата
     */
    BigDecimal maxSalary;

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
    LocalDateTime updatedAt;

    /**
     * Статус последнего выполнения задачи
     */
    TaskResultStatus status;
}
