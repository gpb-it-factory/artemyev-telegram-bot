package com.gpb.service;

import com.gpb.strategy.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CommandStrategyHandler {
    @Qualifier("defaultCommand")
    private final CommandStrategy defaultCommandStrategy;
    private final Map<String, CommandStrategy> commandStrategies = new HashMap<>();

    public CommandStrategyHandler(List<IdentifiableCommand> strategies, DefaultCommand defaultCommand) {
        for (IdentifiableCommand strategy : strategies) {
            String command = strategy.getCommand();
            if (commandStrategies.containsKey(command)) {
                log.error("Duplicate strategy for command: " + command);
            }
            else {
                commandStrategies.put(command, strategy);
            }
        }
        this.defaultCommandStrategy = defaultCommand;
    }

    public CommandStrategy getStrategy(Message message) {
        String messageText = message.getText();
        if (messageText.startsWith("/")) {
            return commandStrategies.getOrDefault(messageText, defaultCommandStrategy);
        }
        return defaultCommandStrategy;
    }
}
