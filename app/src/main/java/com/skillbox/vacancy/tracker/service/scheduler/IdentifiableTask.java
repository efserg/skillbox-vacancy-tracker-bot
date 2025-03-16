package com.skillbox.vacancy.tracker.service.scheduler;

public interface IdentifiableTask extends Runnable {
    default String getTaskId() {
        return getTaskId(getUserId(), getChatId());
    }

    static String getTaskId(Long userId, Long chatId) {
        return "%d_%d".formatted(userId, chatId);
    }

    Long getUserId();

    Long getChatId();

}
