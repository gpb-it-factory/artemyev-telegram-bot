package com.gpb.strategy;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Slf4j
public class StrategyContext {
    private CommandProcessingStrategy strategy;
    private final TelegramLongPollingBot bot;

    public StrategyContext(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public void setStrategy(CommandProcessingStrategy strategy) {
        this.strategy = strategy;
    }

    public void processMessage(Message message) {
        String responseText = strategy.process(message);
        sendResponse(message.getChatId(), responseText);
    }

    private void sendResponse(Long chatId,@NonNull String text) {
        SendMessage response = new SendMessage(chatId.toString(), text);
        try {
            bot.execute(response);
        } catch (TelegramApiException e) {
            log.error("Failed to send response message", e);
        }
    }
}
