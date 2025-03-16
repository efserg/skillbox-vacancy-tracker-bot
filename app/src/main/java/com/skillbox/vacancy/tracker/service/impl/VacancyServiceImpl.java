package com.skillbox.vacancy.tracker.service.impl;

import com.skillbox.vacancy.tracker.model.Vacancy;
import com.skillbox.vacancy.tracker.repository.VacancyRepository;
import com.skillbox.vacancy.tracker.service.VacancyService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;

    @Override
    public List<Vacancy> find(Long userId, Long chatId) {
        return vacancyRepository.findAll(userId, chatId);
    }

    @Override
    public void saveAll(Long userId, Long chatId, List<Vacancy> vacancies) {
        vacancyRepository.saveAll(userId, chatId, vacancies);
    }
}
