package com.skillbox.vacancy.tracker.repository.impl;

import com.skillbox.vacancy.tracker.model.NotificationTask;
import java.util.Optional;
import java.util.UUID;

public class JsonNotificationTaskRepository extends AbstractJsonTaskRepository<NotificationTask> {

    @Override
    public Optional<NotificationTask> findById(Long userId, Long chatId) {
        return findById(userId, chatId, NotificationTask.class);
    }

    @Override
    protected String getDirectoryPath() {
        return "notification_task";
    }
}
