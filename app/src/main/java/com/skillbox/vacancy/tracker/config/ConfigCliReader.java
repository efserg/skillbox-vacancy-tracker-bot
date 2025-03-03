package com.skillbox.vacancy.tracker.config;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfigCliReader implements ConfigReader {

    private final String[] args;

    @Override
    public Config read() {
        return new Config(args[1], args[2]);
    }
}
