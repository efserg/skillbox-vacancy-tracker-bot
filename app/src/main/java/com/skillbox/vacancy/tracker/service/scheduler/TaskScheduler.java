package com.skillbox.vacancy.tracker.service.scheduler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaskScheduler {

    private final ScheduledExecutorService scheduler;

    private final Map<String, List<ScheduledFuture<?>>> scheduledTasks =  new ConcurrentHashMap<>();;

    public void cancelTask(String taskId) {
        List<ScheduledFuture<?>> scheduled = scheduledTasks.remove(taskId);
        if (scheduled != null) {
            scheduled.forEach(task -> task.cancel(true));
        }
    }
    public void scheduleDailyTask(LocalTime targetTime, IdentifiableTask task) {
        long initialDelay = calculateInitialDelay(targetTime);
        long period = TimeUnit.DAYS.toSeconds(1);
        final ScheduledFuture<?> scheduled = scheduler.scheduleAtFixedRate(task, initialDelay, period,
                TimeUnit.SECONDS);
        scheduledTasks.computeIfAbsent(task.getTaskId(), k -> new ArrayList<>()).add(scheduled);
    }

    private long calculateInitialDelay(LocalTime targetTime) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime nextRun = now.with(targetTime);

        if (now.isAfter(nextRun)) {
            nextRun = nextRun.plusDays(1);
        }

        return Duration.between(now, nextRun).getSeconds();
    }

}
