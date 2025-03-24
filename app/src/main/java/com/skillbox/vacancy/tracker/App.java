package com.skillbox.vacancy.tracker;

import com.skillbox.vacancy.tracker.bot.BotCommandStorage;
import com.skillbox.vacancy.tracker.bot.command.MainMenuCommand;
import com.skillbox.vacancy.tracker.bot.command.ReadyCommand;
import com.skillbox.vacancy.tracker.bot.command.SetExperienceCommand;
import com.skillbox.vacancy.tracker.bot.command.SetKeywordCommand;
import com.skillbox.vacancy.tracker.bot.command.SetNotificationTimeCommand;
import com.skillbox.vacancy.tracker.bot.command.SetRegionCommand;
import com.skillbox.vacancy.tracker.bot.command.SetSalaryCommand;
import com.skillbox.vacancy.tracker.bot.command.SetTimeZoneCommand;
import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import com.skillbox.vacancy.tracker.model.NotificationTask;
import com.skillbox.vacancy.tracker.repository.TaskRepository;
import com.skillbox.vacancy.tracker.repository.UserRepository;
import com.skillbox.vacancy.tracker.repository.VacancyRepository;
import com.skillbox.vacancy.tracker.repository.impl.JsonNotificationTaskRepository;
import com.skillbox.vacancy.tracker.repository.impl.JsonUserRepository;
import com.skillbox.vacancy.tracker.repository.impl.JsonVacancyRepository;
import com.skillbox.vacancy.tracker.repository.impl.JsonVacancyTaskRepository;
import com.skillbox.vacancy.tracker.service.FindVacancyTaskService;
import com.skillbox.vacancy.tracker.service.NotificationTaskService;
import com.skillbox.vacancy.tracker.service.impl.NotificationTaskServiceImpl;
import com.skillbox.vacancy.tracker.service.UserService;
import com.skillbox.vacancy.tracker.service.VacancyService;
import com.skillbox.vacancy.tracker.service.impl.FindVacancyTaskServiceImpl;
import com.skillbox.vacancy.tracker.service.impl.VacancyServiceImpl;
import com.skillbox.vacancy.tracker.service.scheduler.TaskScheduler;
import com.skillbox.vacancy.tracker.service.scheduler.UserNotificator;
import com.skillbox.vacancy.tracker.service.scheduler.VacancyLoader;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import com.skillbox.vacancy.tracker.bot.BotCommandExecutor;
import com.skillbox.vacancy.tracker.bot.command.StartCommand;
import com.skillbox.vacancy.tracker.bot.command.StopCommand;
import com.skillbox.vacancy.tracker.bot.VacancyTrackerBot;
import com.skillbox.vacancy.tracker.config.Config;
import com.skillbox.vacancy.tracker.config.ConfigCliReader;
import com.skillbox.vacancy.tracker.service.impl.UserServiceImpl;

public class App {

    public static void main(String[] args) {
        // Загружаем конфигурацию (токен, имя бота и т.д.)
        Config config = new ConfigCliReader(args).read();
        String botToken = config.botApiToken();

        // Инициализируем зависимости
        OkHttpTelegramClient telegramClient = new OkHttpTelegramClient(botToken);

        final UserRepository userRepository = new JsonUserRepository();
        final TaskRepository<NotificationTask> notificationTaskRepository = new JsonNotificationTaskRepository();
        final TaskRepository<FindVacancyTask> findVacancyTaskTaskRepository = new JsonVacancyTaskRepository();
        final VacancyRepository vacancyRepository = new JsonVacancyRepository();

        final VacancyService vacancyService = new VacancyServiceImpl(vacancyRepository);
        final FindVacancyTaskService findVacancyTaskService = new FindVacancyTaskServiceImpl(
                findVacancyTaskTaskRepository);
        final NotificationTaskService notificationTaskService = new NotificationTaskServiceImpl(
                notificationTaskRepository);

        final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

        final TaskScheduler taskScheduler = new TaskScheduler(scheduledExecutorService);

        final UserService userService = new UserServiceImpl(
                taskScheduler, userRepository,
                findVacancyTaskTaskRepository,
                notificationTaskRepository, vacancyRepository);

        final UserNotificator userNotificator = new UserNotificator(
                taskScheduler, vacancyService,
                notificationTaskService, telegramClient);

        final VacancyLoader vacancyLoader = new VacancyLoader(
                taskScheduler, vacancyService,
                findVacancyTaskService);

        final BotCommandStorage storage = new BotCommandStorage();
        storage.put(new StopCommand(userService))
                .put(new StartCommand(userService, findVacancyTaskService))
                .put(new MainMenuCommand(findVacancyTaskService, notificationTaskService))
                .put(new SetRegionCommand(findVacancyTaskService, storage))
                .put(new SetExperienceCommand(findVacancyTaskService, storage))
                .put(new SetSalaryCommand(findVacancyTaskService, storage))
                .put(new SetKeywordCommand(findVacancyTaskService, storage))
                .put(new SetNotificationTimeCommand(notificationTaskService, storage))
                .put(new SetTimeZoneCommand(userService, storage))
                .put(new ReadyCommand(userNotificator, vacancyLoader));

        final BotCommandExecutor commandExecutor = new BotCommandExecutor(storage);

        try (var botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new VacancyTrackerBot(telegramClient, commandExecutor));
            System.out.println("Bot is running!");
            userNotificator.scheduleTask(198568064L, 198568064L);
            Thread.currentThread().join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
