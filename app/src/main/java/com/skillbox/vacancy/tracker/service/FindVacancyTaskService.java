package com.skillbox.vacancy.tracker.service;

import com.skillbox.vacancy.tracker.model.FindVacancyTask;

public interface FindVacancyTaskService {
    FindVacancyTask find(Long userId, Long chatId);

    void save(FindVacancyTask task);

    boolean exists(Long userId, Long chatId);
}
