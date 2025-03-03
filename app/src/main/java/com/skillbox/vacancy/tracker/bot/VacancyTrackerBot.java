package com.skillbox.vacancy.tracker.bot;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class VacancyTrackerBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final BotCommandExecutor commandExecutor;

    public VacancyTrackerBot(TelegramClient telegramClient, BotCommandExecutor commandExecutor) {
        this.telegramClient = telegramClient;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void consume(Update update) {
        final SendMessage message = commandExecutor.execute(update);
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}