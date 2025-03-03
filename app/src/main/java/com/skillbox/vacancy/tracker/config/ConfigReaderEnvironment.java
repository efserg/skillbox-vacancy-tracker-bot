package com.skillbox.vacancy.tracker.config;

public class ConfigReaderEnvironment implements ConfigReader {
    public Config read() {
        String name = System.getenv("BOT_NAME");
        String token = System.getenv("BOT_TOKEN");
        return new Config(name, token);
    }
}
