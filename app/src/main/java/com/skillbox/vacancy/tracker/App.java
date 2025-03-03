package com.skillbox.vacancy.tracker;

import java.util.List;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import com.skillbox.vacancy.tracker.bot.BotCommand;
import com.skillbox.vacancy.tracker.bot.BotCommandExecutor;
import com.skillbox.vacancy.tracker.bot.StartCommand;
import com.skillbox.vacancy.tracker.bot.StopCommand;
import com.skillbox.vacancy.tracker.bot.VacancyTrackerBot;
import com.skillbox.vacancy.tracker.config.Config;
import com.skillbox.vacancy.tracker.config.ConfigCliReader;
import com.skillbox.vacancy.tracker.service.impl.JsonFileUserService;

public class App {
    public static void main(String[] args) {
        // Загружаем конфигурацию (токен, имя бота и т.д.)
        Config config = new ConfigCliReader(args).read();
        String botToken = config.botApiToken();

        // Инициализируем зависимости
        var telegramClient = new OkHttpTelegramClient(botToken);
        final List<BotCommand> commands = List.of(new StopCommand(), new StartCommand(new JsonFileUserService()));
        final BotCommandExecutor commandExecutor = new BotCommandExecutor(commands);

        try (var botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new VacancyTrackerBot(telegramClient, commandExecutor));
            System.out.println("Bot is running!");
            Thread.currentThread().join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
