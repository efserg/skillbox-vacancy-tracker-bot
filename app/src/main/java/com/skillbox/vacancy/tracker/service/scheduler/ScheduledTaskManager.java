package com.skillbox.vacancy.tracker.service.scheduler;

public interface ScheduledTaskManager {

    void scheduleTask(Long userId, Long chatId);

    void removeTask(Long userId, Long chatId);

}
