package com.gpb.strategy;

import com.gpb.constant.BotCommand;
import com.gpb.constant.BotMessage;
import com.gpb.service.MessageSenderService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public final class StartCommand implements IdentifiableCommand {
    private MessageSenderService messageSenderService;

    public StartCommand(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @Override
    public SendMessage process(Message message) {
        String welcomeText = String.format(BotMessage.WELCOME_MESSAGE.getText(), message.getFrom().getFirstName());
        return messageSenderService.sendMessage(message.getChatId(), welcomeText);
    }

    @Override
    public String getCommand() {
        return BotCommand.START.getCommand();
    }


}
