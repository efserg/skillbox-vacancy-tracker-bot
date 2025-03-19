package com.skillbox.vacancy.tracker.repository.impl;

import com.skillbox.vacancy.tracker.model.BotTask;
import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import com.skillbox.vacancy.tracker.model.NotificationTask;
import com.skillbox.vacancy.tracker.repository.TaskRepository;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractJsonTaskRepository<T extends BotTask>
        extends AbstractJsonRepository implements TaskRepository<T> {

    private static final String FILENAME_PATTERN = "%s/%d_%d.json";

    @Override
    public void save(T task) {
        if (task == null
                || task.getChatId() == null
                || task.getUserId() == null) {
            throw new IllegalArgumentException("Task, user ID или chat ID не могут быть null");
        }

        String fileName = getFileName(task.getUserId(), task.getChatId());

        save(fileName, task);
    }

    protected Optional<T> findById(Long userId, Long chatId, Class<T> clazz) {
        if (userId == null || chatId == null) {
            throw new IllegalArgumentException("User ID или chat ID не могут быть null");
        }
        return load(getFileName(userId, chatId), clazz);
    }

    @Override
    public void delete(Long userId, Long chatId) {
        super.delete(getFileName(userId, chatId));
    }

    @Override
    public boolean exists(Long userId, Long chatId) {
        return exists(getFileName(userId, chatId));
    }

    @Override
    protected abstract String getDirectoryPath();

    protected String getFileName(Long userId, Long chatId) {
        return FILENAME_PATTERN.formatted(getDirectoryPath(), userId, chatId);
    }

}
