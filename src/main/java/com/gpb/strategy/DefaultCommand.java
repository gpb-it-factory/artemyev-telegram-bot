package com.gpb.strategy;

import com.gpb.constant.BotMessage;
import com.gpb.service.MessageSenderService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public final class DefaultCommand implements CommandStrategy {
    private final MessageSenderService messageSenderService;

    public DefaultCommand(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @Override
    public SendMessage process(Message message) {
        return messageSenderService.sendMessage(message.getChatId(), BotMessage.DEFAULT_MESSAGE.getText());

    }
}
