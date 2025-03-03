package com.skillbox.vacancy.tracker.config;

public record Config(String botName, String botApiToken) {
    public Config {
        if (botName == null || botName.isEmpty()) {
            throw new RuntimeException("Имя бота не задано!");
        }
        if (botApiToken == null || botApiToken.isEmpty()) {
            throw new RuntimeException("Токен не задан!");
        }
    }
}
