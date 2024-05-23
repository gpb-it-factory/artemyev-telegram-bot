package com.gpb.service;

import com.gpb.exception.MessageSendingException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class MessageSenderService {

    private final TelegramLongPollingBot bot;

    public MessageSenderService(@Lazy TelegramLongPollingBot bot) {
        this.bot = bot;
    }
    public SendMessage sendMessage(Long chatId, @NonNull String text) throws MessageSendingException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            bot.execute(message);
            return message;
        } catch (TelegramApiException e) {
            log.error("Error sending message to chatId: " + chatId, e);
            throw new MessageSendingException("Error sending message to chatId: " + chatId, e);
        }
    }
}