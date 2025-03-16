package com.skillbox.vacancy.tracker.repository.impl;

import com.skillbox.vacancy.tracker.model.BotUser;
import com.skillbox.vacancy.tracker.repository.UserRepository;
import com.skillbox.vacancy.tracker.repository.impl.AbstractJsonRepository;
import java.util.Optional;

public class JsonUserRepository extends AbstractJsonRepository implements UserRepository {

    private static final String FILENAME_PATTERN = "%s/%d.json";

    @Override
    public Optional<BotUser> findById(Long id) {
        return load(getFilename(id), BotUser.class);
    }

    @Override
    public boolean exists(Long id) {
        return super.exists(getFilename(id));
    }

    private String getFilename(Long id) {
        return FILENAME_PATTERN.formatted(getDirectoryPath(), id);
    }

    @Override
    public void save(BotUser user) {
        save(getFilename(user.getId()), user);
    }

    @Override
    public void delete(Long id) {
        delete(getFilename(id));
    }

    @Override
    protected String getDirectoryPath() {
        return "user";
    }
}
