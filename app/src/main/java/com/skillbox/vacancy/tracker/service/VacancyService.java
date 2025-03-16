package com.skillbox.vacancy.tracker.service;

import com.skillbox.vacancy.tracker.model.Vacancy;
import java.util.List;

public interface VacancyService {
    List<Vacancy> find(Long userId, Long chatId);

    void saveAll(Long userId, Long chatId, List<Vacancy> vacancies);
}
