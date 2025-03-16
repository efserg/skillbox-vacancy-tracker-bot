package com.skillbox.vacancy.tracker.service.scheduler;

import com.skillbox.vacancy.tracker.service.FindVacancyTaskService;
import com.skillbox.vacancy.tracker.service.VacancyService;
import java.net.http.HttpClient;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VacancyLoader implements ScheduledTaskManager {

    private final TaskScheduler taskScheduler;

    private final VacancyService vacancyService;

    private final FindVacancyTaskService vacancyTaskService;
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    @Override
    public void scheduleTask(Long userId, Long chatId) {
        final VacancyLoadTask task = new VacancyLoadTask(userId, chatId, HTTP_CLIENT, vacancyService,
                vacancyTaskService);
        taskScheduler.scheduleDailyTask(LocalTime.of(23, 0), task);
    }

    @Override
    public void removeTask(Long userId, Long chatId) {
        taskScheduler.cancelTask(IdentifiableTask.getTaskId(userId, chatId));
    }

}
