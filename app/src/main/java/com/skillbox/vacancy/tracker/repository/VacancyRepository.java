package com.skillbox.vacancy.tracker.repository;

import com.skillbox.vacancy.tracker.model.Vacancy;
import java.util.List;
import java.util.UUID;

public interface VacancyRepository {

    void saveAll(Long userId, Long chatId, Iterable<Vacancy> vacancies);

    List<Vacancy> findAll(Long userId, Long chatId);

    void deleteAll(Long userId, Long chatId);
}
