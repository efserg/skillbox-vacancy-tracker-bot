package com.skillbox.vacancy.tracker.service.impl;

import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import com.skillbox.vacancy.tracker.repository.TaskRepository;
import com.skillbox.vacancy.tracker.service.FindVacancyTaskService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindVacancyTaskServiceImpl implements FindVacancyTaskService {

    private final TaskRepository<FindVacancyTask> repository;

    @Override
    public FindVacancyTask find(Long userId, Long chatId) {
        return repository.findById(userId, chatId).orElseThrow();
    }

    @Override
    public void save(FindVacancyTask task) {
        repository.save(task);
    }
}
