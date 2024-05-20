package com.gpb.service;

import com.gpb.config.BotConfig;
import com.gpb.strategy.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final CommandStrategyService commandStrategyService;

    public TelegramBot(BotConfig config, CommandStrategyService commandStrategyService) {
        this.config = config;
        this.commandStrategyService = commandStrategyService;
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
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage());
        }
    }

    private void handleMessage(Message message) {
        CommandProcessingStrategy strategy = commandStrategyService.getStrategy(message);

        StrategyContext strategyContext = new StrategyContext(this);
        strategyContext.setStrategy(strategy);
        strategyContext.processMessage(message);
    }
}
