package com.skillbox.vacancy.tracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private String status;
    private Request request;
    private Meta meta;
    private Results results;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String api;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {

        private int total;
        private int limit;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Results {

        private List<VacancyWrapper> vacancies;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VacancyWrapper {

        private Vacancy vacancy;
    }

}