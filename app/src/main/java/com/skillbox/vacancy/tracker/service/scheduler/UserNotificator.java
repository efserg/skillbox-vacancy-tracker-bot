package com.skillbox.vacancy.tracker.service.scheduler;

import com.skillbox.vacancy.tracker.model.NotificationTask;
import com.skillbox.vacancy.tracker.service.NotificationTaskService;
import com.skillbox.vacancy.tracker.service.VacancyService;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@RequiredArgsConstructor
public class UserNotificator implements ScheduledTaskManager {

    private final TaskScheduler taskScheduler;

    private final VacancyService vacancyService;

    private final NotificationTaskService taskService;

    private final TelegramClient telegramClient;

    @Override
    public void scheduleTask(Long userId, Long chatId) {
        final NotificationTask task = taskService.find(userId, chatId);
        final LocalTime notificationTime = task.getNotificationTime();
        taskScheduler.scheduleDailyTask(notificationTime,
                new UserNotificatorTask(userId, chatId, vacancyService, telegramClient));
    }

    @Override
    public void removeTask(Long userId, Long chatId) {
        taskScheduler.cancelTask(IdentifiableTask.getTaskId(userId, chatId));
    }
}
