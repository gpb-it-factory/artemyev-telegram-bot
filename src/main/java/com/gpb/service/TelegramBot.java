package com.gpb.service;

import com.gpb.config.BotConfig;
import com.gpb.strategy.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final CommandStrategyHandler commandStrategyHandler;

    public TelegramBot(BotConfig config, CommandStrategyHandler commandStrategyHandler) {
        this.config = config;
        this.commandStrategyHandler = commandStrategyHandler;
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
        CommandStrategy strategy = commandStrategyHandler.getStrategy(message);

        StrategyContext strategyContext = new StrategyContext();
        strategyContext.setStrategy(strategy);
        strategyContext.processMessage(message);
    }
}
