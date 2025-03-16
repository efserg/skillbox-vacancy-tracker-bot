package com.skillbox.vacancy.tracker.repository.impl;

import com.google.gson.reflect.TypeToken;
import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import com.skillbox.vacancy.tracker.model.Vacancy;
import com.skillbox.vacancy.tracker.repository.VacancyRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JsonVacancyRepository extends AbstractJsonRepository implements VacancyRepository {

    public static final String FILE_PATH = "%s/%d_%d.json";

    @Override
    public void saveAll(Long userId, Long chatId, Iterable<Vacancy> vacancies) {
        if (userId == null || chatId == null || vacancies == null) {
            throw new IllegalArgumentException("User ID, chat ID и список вакансий не могут быть null");
        }

        String fileName = getFileName(userId, chatId);

        save(fileName, vacancies);

    }

    @Override
    public List<Vacancy> findAll(Long userId, Long chatId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID не может быть null");
        }
        final Optional<List<Vacancy>> load = load(getFileName(userId, chatId), new TypeToken<List<Vacancy>>() {
        }.getType());
        return load.orElse(List.of());
    }

    @Override
    public void deleteAll(Long userId, Long chatId) {
        delete(getFileName(userId, chatId));
    }

    @Override
    protected String getDirectoryPath() {
        return "vacancies";
    }

    private String getFileName(Long userId, Long chatId) {
        return FILE_PATH.formatted(getDirectoryPath(), userId, chatId);
    }

}
