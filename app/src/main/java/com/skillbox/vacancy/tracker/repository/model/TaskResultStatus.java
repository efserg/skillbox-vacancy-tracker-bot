package com.skillbox.vacancy.tracker.repository.model;

public enum TaskResultStatus {
    /**
     * Успешно выполнено
     */
    SUCCESS,

    /**
     * Ошибка выполнения (последующий запуск экзекутора задачи попробует ее перезапустить)
     */
    ERROR,

    /**
     * Ожидание в очереди выполнения
     */
    WAIT,

    /**
     * Выполняется сейчас
     */
    RUNNING,

    /**
     * Отменена пользователем (но не удалена)
     */
    STOP
}
