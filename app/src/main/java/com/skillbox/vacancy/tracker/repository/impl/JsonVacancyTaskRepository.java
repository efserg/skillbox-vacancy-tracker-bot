package com.skillbox.vacancy.tracker.repository.impl;

import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import java.util.Optional;

public class JsonVacancyTaskRepository extends AbstractJsonTaskRepository<FindVacancyTask> {

    @Override
    public Optional<FindVacancyTask> findById(Long userId, Long chatId) {
        return findById(userId, chatId, FindVacancyTask.class);
    }

    @Override
    protected String getDirectoryPath() {
        return "vacancy_task";
    }
}
