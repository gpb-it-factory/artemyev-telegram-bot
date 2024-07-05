package com.gpb.handler;

import com.gpb.exception.MessageSendingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
@Slf4j
public class TelegramHandler {
    private final CommandStrategyHandler commandStrategyHandler;

    public SendMessage handleUpdate(Update update) {
        SendMessage message = null;
        if (update.getMessage() == null) {
            return new SendMessage(update.getMessage().getChatId().toString(), "Received update without a message");
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                message = commandStrategyHandler.getStrategy(update.getMessage()).process(update);
            } catch (MessageSendingException e) {
                return new SendMessage(update.getMessage().getChatId().toString(), e.getMessage());
            }
        }
        return message;
    }
}
