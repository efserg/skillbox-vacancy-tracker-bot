package com.skillbox.vacancy.tracker.service.impl;

import java.io.File;

import com.google.gson.Gson;
import com.skillbox.vacancy.tracker.repository.model.BotUser;
import com.skillbox.vacancy.tracker.service.UserService;

public class JsonFileUserService implements UserService {

    private static final String USER_FOLDER = "%d" + File.separator;
    private static final String USER_FILE =  USER_FOLDER + "%d_user-info.json";

    private final Gson gson;

    public JsonFileUserService() {
        gson = new Gson();
    }

    @Override
    public BotUser save(BotUser user) {
        return null;
    }

    @Override
    public BotUser find(Long id) {

        return null;
    }

    @Override
    public boolean exist(Long id) {
        final String userFilename = USER_FILE.formatted(id, id);
        File file = new File(userFilename);
        return file.exists();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public BotUser update() {
        return null;
    }
}
