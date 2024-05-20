package com.gpb.strategy;

import com.gpb.constant.BotCommand;
import com.gpb.constant.BotMessage;
import com.gpb.service.MessageSenderService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public final class StartCommandStrategy implements IdentifiableCommandProcessingStrategy {
    private final MessageSenderService messageSenderService;

    public StartCommandStrategy(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @Override
    public void process(Message message) {
        messageSenderService.sendMessage(message.getChatId(), BotMessage.WELCOME_MESSAGE.getText());
    }
    @Override
    public String getCommand() {
        return BotCommand.START.getCommand();
    }
}
