package com.skillbox.vacancy.tracker.service.scheduler;

import com.skillbox.vacancy.tracker.model.BotTask;

public interface ScheduledTaskManager {

    BotTask scheduleTask(Long userId, Long chatId);

}
