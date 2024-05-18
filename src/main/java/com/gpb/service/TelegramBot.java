package com.gpb.service;

import com.gpb.config.BotConfig;
import com.gpb.strategy.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final Map<String, CommandProcessingStrategy> commandStrategies;


    @Autowired
    public TelegramBot(BotConfig config, Map<String, CommandProcessingStrategy> commandStrategies) {
        this.config = config;
        this.commandStrategies = initializeCommandStrategies();
    }

    private Map<String, CommandProcessingStrategy> initializeCommandStrategies() {
        Map<String, CommandProcessingStrategy> strategies = new HashMap<>();
        strategies.put("/start", new StartCommandStrategy());
        strategies.put("/ping", new PingCommandStrategy());

        return strategies;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() == null) {
            log.info("Received update without a message");
            return;
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage());
        }
    }

    private void handleMessage(Message message) {
        String messageText = message.getText();
        final StrategyContext strategyContext = new StrategyContext(this);

        CommandProcessingStrategy strategy = commandStrategies.getOrDefault(
                messageText,
                new DefaultCommandStrategy()
        );

        strategyContext.setStrategy(strategy);
        strategyContext.processMessage(message);
    }
}
