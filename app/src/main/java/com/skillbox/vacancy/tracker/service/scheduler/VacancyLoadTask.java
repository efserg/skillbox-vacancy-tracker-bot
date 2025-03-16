package com.skillbox.vacancy.tracker.service.scheduler;

import com.google.gson.Gson;
import com.skillbox.vacancy.tracker.model.ApiResponse;
import com.skillbox.vacancy.tracker.model.ApiResponse.VacancyWrapper;
import com.skillbox.vacancy.tracker.model.TaskResultStatus;
import com.skillbox.vacancy.tracker.model.FindVacancyTask;
import com.skillbox.vacancy.tracker.model.Vacancy;
import com.skillbox.vacancy.tracker.repository.impl.JsonVacancyRepository;
import com.skillbox.vacancy.tracker.repository.impl.JsonVacancyTaskRepository;
import com.skillbox.vacancy.tracker.service.FindVacancyTaskService;
import com.skillbox.vacancy.tracker.service.VacancyService;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public class VacancyLoadTask implements IdentifiableTask {

    private static final String BASE_URL = "https://opendata.trudvsem.ru/api/v1/vacancies";
    private static final int PAGE_SIZE = 100;

    private final Long userId;

    private final Long chatId;

    private final HttpClient httpClient;

    private final VacancyService vacancyService;

    private final FindVacancyTaskService taskService;

    @Override
    public void run() {

        FindVacancyTask task = taskService.find(userId, chatId);
        task.setStatus(TaskResultStatus.RUNNING);
        taskService.save(task);

        String fullUrl = BASE_URL;

        if (task.getRegion() != null) {
            fullUrl += "/region/" + task.getRegion();
        }
        String queryParams = buildParams(task);

        int start = 0;
        int total;
        Stream<Vacancy> vacancyStream = Stream.empty();
        do {
            String url = fullUrl + (queryParams + "&offset=" + start);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                    .header("Accept-encoding", "gzip, deflate, br, zstd")
                    .header("Accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                    .GET()
                    .build();
            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    String json = response.body();
                    Gson gson = new Gson();
                    ApiResponse apiResponse = gson.fromJson(json, ApiResponse.class);
                    vacancyStream = Stream.concat(vacancyStream, extractVacancies(apiResponse, task));
                    total = apiResponse.getMeta().getTotal();
                    start += PAGE_SIZE;
                } else {
                    throw new RuntimeException("Error " + response.statusCode() + " " + response.body());
                }
            } catch (Exception e) {
                task.setStatus(TaskResultStatus.ERROR);
                taskService.save(task);
                return;
            }
        } while (start < total);

        vacancyService.saveAll(userId, chatId, vacancyStream.collect(Collectors.toList()));

        task.setUpdatedAt(Instant.now());
        task.setStatus(TaskResultStatus.SUCCESS);
        taskService.save(task);
    }

    @NotNull
    private Stream<Vacancy> extractVacancies(ApiResponse apiResponse, FindVacancyTask task) {
        if (apiResponse == null || apiResponse.getResults() == null
                || apiResponse.getResults().getVacancies() == null) {
            return Stream.empty();
        }
        return apiResponse.getResults().getVacancies().stream().map(VacancyWrapper::getVacancy)
                .filter(v -> task.getSalaryMin() == null || v.getSalaryMin() == null
                        || v.getSalaryMin() >= task.getSalaryMin());
    }

    private String buildParams(FindVacancyTask task) {
        final String lastUpdatedValue = task.getUpdatedAt() == null
                ? null
                : (task.getUpdatedAt().atOffset(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ISO_INSTANT));
        return Stream.of(
                        formatParam("experienceFrom", task.getExperienceFrom()),
                        formatParam("experienceTo", task.getExperienceTo()),
                        formatParam("text", task.getKeyword()),
                        formatParam("modifiedFrom", lastUpdatedValue),
                        formatParam("limit", PAGE_SIZE)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.joining("&", "?", ""));
    }

    private String formatParam(String key, Object value) {
        return (value == null)
                ? null
                : key + "=" + URLEncoder.encode(value.toString(), StandardCharsets.UTF_8);
    }

}
