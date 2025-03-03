package com.skillbox.vacancy.tracker.repository.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TaskResult implements Serializable {

    /**
     * Время старта задачи
     */
    Instant startAt;

    /**
     * Время окончания задачи
     */
    Instant endAt;

    /**
     * Список вакансий, полученных ботом
     */
    List<Vacancy> vacancies;

}
