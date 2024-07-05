package com.gpb.handler;

import com.gpb.cache.UserDataCache;
import com.gpb.constant.BotCommands;
import com.gpb.strategy.CommandStrategy;
import com.gpb.strategy.IdentifiableCommand;
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
    private final CommandStrategy defaultCommandStrategy;
    private final UserDataCache userDataCache;
    private final Map<String, CommandStrategy> commandStrategies = new HashMap<>();

    public CommandStrategyHandler(List<IdentifiableCommand> strategies, @Qualifier("defaultCommand") CommandStrategy defaultCommand, UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
        for (IdentifiableCommand strategy : strategies) {
            String command = strategy.getCommand();
            if (commandStrategies.containsKey(command)) {
                log.error("Duplicate strategy for command: " + command);
            }
            commandStrategies.put(command, strategy);
        }
        this.defaultCommandStrategy = defaultCommand;
    }

    public CommandStrategy getStrategy(Message message) {
        String messageText = message.getText();
        long chatId = message.getChatId();

        if (messageText.startsWith("/")) {
            return commandStrategies.getOrDefault(messageText, defaultCommandStrategy);
        }
        boolean isTransferCommand = userDataCache.getRecipientName(chatId) != null ||
                userDataCache.getAmount(chatId) != null ||
                userDataCache.getUserBotState(chatId) != null;

        if (isTransferCommand) {
            return commandStrategies.get(BotCommands.TRANSFER.getCommand());
        }
        return defaultCommandStrategy;
    }
}
