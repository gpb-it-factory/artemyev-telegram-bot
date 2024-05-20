package com.gpb.strategy;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Slf4j
public class StrategyContext {
    private CommandProcessingStrategy strategy;
    private TelegramLongPollingBot bot;

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

    private void sendResponse(Long chatId, String text) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());
        response.setText(text);
        try {
            bot.execute(response); // Вызов метода execute для отправки сообщения
        } catch (TelegramApiException e) {
            log.error("Failed to send response message", e);
        }
    }
}
