package com.skillbox.vacancy.tracker.service;

import com.skillbox.vacancy.tracker.model.BotUser;

public interface UserService {
    BotUser save(BotUser user);

    BotUser find(Long id);

    boolean exist(Long id);

    void delete(Long id);

}
