package com.gpb.strategy;

import com.gpb.exception.MessageProcessingException;
import com.gpb.exception.MessageSendingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class StrategyContext {
    private CommandStrategy strategy;

    public void setStrategy(CommandStrategy strategy) {
        this.strategy = strategy;
    }

    public void processMessage(Message message) {
        try {
            strategy.process(message);
        } catch (MessageSendingException e) {
            throw new MessageProcessingException("Error processing message: " + message.getText(), e);
        }
    }
}
