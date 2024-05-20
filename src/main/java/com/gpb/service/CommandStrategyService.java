package com.gpb.service;

import com.gpb.strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandStrategyService {
    private final DefaultCommandStrategy defaultCommandStrategy;
    private final Map<String, CommandProcessingStrategy> commandStrategies = new HashMap<>();

    public CommandStrategyService(List<CommandProcessingStrategy> strategies, DefaultCommandStrategy defaultCommandStrategy) {
        this.defaultCommandStrategy = defaultCommandStrategy;
        for (CommandProcessingStrategy strategy : strategies) {
            if (strategy instanceof IdentifiableCommandProcessingStrategy) {
                IdentifiableCommandProcessingStrategy identifiable = (IdentifiableCommandProcessingStrategy) strategy;
                commandStrategies.put(identifiable.getCommand(), strategy);
            }
        }
    }

    public CommandProcessingStrategy getStrategy(Message message) {
        String messageText = message.getText();
        if (messageText.startsWith("/")) {
            return commandStrategies.getOrDefault(messageText, defaultCommandStrategy);

        }
        return defaultCommandStrategy;
    }
}
