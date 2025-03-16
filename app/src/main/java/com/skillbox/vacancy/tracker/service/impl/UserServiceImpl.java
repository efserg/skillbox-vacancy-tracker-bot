package com.skillbox.vacancy.tracker.service.impl;

import com.skillbox.vacancy.tracker.exception.UserNotFoundException;
import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import com.skillbox.vacancy.tracker.model.NotificationTask;
import com.skillbox.vacancy.tracker.repository.TaskRepository;
import com.skillbox.vacancy.tracker.repository.UserRepository;

import com.skillbox.vacancy.tracker.model.BotUser;
import com.skillbox.vacancy.tracker.repository.VacancyRepository;
import com.skillbox.vacancy.tracker.service.UserService;
import com.skillbox.vacancy.tracker.service.scheduler.IdentifiableTask;
import com.skillbox.vacancy.tracker.service.scheduler.TaskScheduler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final TaskScheduler taskScheduler;
    private final UserRepository userRepository;
    private final TaskRepository<FindVacancyTask> findVacancyTaskTaskRepository;
    private final TaskRepository<NotificationTask> notificationTaskTaskRepository;
    private final VacancyRepository vacancyRepository;

    @Override
    public BotUser save(BotUser user) {
        userRepository.save(user);
        return find(user.getId());
    }

    @Override
    public BotUser find(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public boolean exist(Long id) {
        return userRepository.exists(id);
    }

    @Override
    public void delete(Long id) {
        final BotUser botUser = find(id);
        botUser.getChats()
                .forEach(chatId -> {
                    taskScheduler.cancelTask(IdentifiableTask.getTaskId(id, chatId));
                    vacancyRepository.deleteAll(id, chatId);
                    findVacancyTaskTaskRepository.delete(id, chatId);
                    notificationTaskTaskRepository.delete(id, chatId);
                });
        userRepository.delete(id);
    }
}
