package com.skillbox.vacancy.tracker.service;

import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import com.skillbox.vacancy.tracker.model.NotificationTask;
import com.skillbox.vacancy.tracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationTaskServiceImpl implements
                                         NotificationTaskService {

    private final TaskRepository<NotificationTask> repository;

    @Override
    public NotificationTask find(Long userId, Long chatId) {
        return repository.findById(userId, chatId).orElseThrow();
    }

    @Override
    public void save(NotificationTask task) {
        repository.save(task);
    }
}
