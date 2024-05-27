package com.gpb.service;

import com.gpb.config.BotConfig;
import com.gpb.exception.MessageSendingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final CommandStrategyHandler commandStrategyHandler;

    public TelegramBot(BotConfig config, CommandStrategyHandler commandStrategyHandler) {
        super(config.getToken());
        this.config = config;
        this.commandStrategyHandler = commandStrategyHandler;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() == null) {
            log.info("Received update without a message");
            return;
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                execute(commandStrategyHandler.getStrategy(update.getMessage()).process(update));
            } catch (MessageSendingException e) {
                log.error("Error processing message: " + update.getMessage().getText(), e);
            } catch (TelegramApiException e) {
                log.error("Error sending message: " + update.getMessage().getText(), e);
            }
        }
    }
}
