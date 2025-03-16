package com.skillbox.vacancy.tracker.service;

import com.skillbox.vacancy.tracker.model.NotificationTask;

public interface NotificationTaskService {
    NotificationTask find(Long userId, Long chatId);

    void save(NotificationTask task);
}
