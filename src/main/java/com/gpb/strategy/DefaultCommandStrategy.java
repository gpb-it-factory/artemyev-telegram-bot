package com.gpb.strategy;

import com.gpb.constant.BotMessage;
import com.gpb.service.MessageSenderService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public final class DefaultCommandStrategy implements CommandProcessingStrategy {
    private final MessageSenderService messageSenderService;

    public DefaultCommandStrategy(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @Override
    public void process(Message message) {
        messageSenderService.sendMessage(message.getChatId(), BotMessage.DEFAULT_MESSAGE.getText());
    }
}
