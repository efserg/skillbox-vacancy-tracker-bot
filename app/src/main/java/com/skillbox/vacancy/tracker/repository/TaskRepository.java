package com.skillbox.vacancy.tracker.repository;

import com.skillbox.vacancy.tracker.model.BotTask;
import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository<T extends BotTask> {

    Optional<T> findById(Long userId, Long chatId);

    void save(T task);

    void delete(Long userId, Long chatId);

}
