package com.skillbox.vacancy.tracker.bot;

import com.skillbox.vacancy.tracker.bot.command.BotCommand;
import com.skillbox.vacancy.tracker.bot.command.UnknownCommand;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BotCommandStorage {

    private final Map<Class<? extends BotCommand>, BotCommand> commands;

    public BotCommandStorage() {
        this.commands = new HashMap<>();
        this.commands.put(UnknownCommand.class, new UnknownCommand());
    }
    public BotCommandStorage(List<BotCommand> commands) {
        this.commands = commands.stream()
                .collect(Collectors.toMap(BotCommand::getClass, Function.identity(), (a, b) -> b, HashMap::new));
    }

    public Collection<BotCommand> getCommandList() {
        return commands.values();
    }

    public BotCommandStorage put(BotCommand command) {
        commands.put(command.getClass(), command);
        return this;
    }

    public BotCommand get(Class<? extends BotCommand> clazz) {
        return commands.get(clazz);
    }
}
