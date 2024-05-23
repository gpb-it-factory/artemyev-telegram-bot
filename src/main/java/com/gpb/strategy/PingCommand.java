package com.gpb.strategy;

import com.gpb.constant.BotCommand;
import com.gpb.constant.BotMessage;
import com.gpb.service.MessageSenderService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public final class PingCommand implements IdentifiableCommand {
    private final MessageSenderService messageSenderService;

    public PingCommand(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @Override
    public SendMessage process(Message message) {
        return messageSenderService.sendMessage(message.getChatId(), BotMessage.ANSWER_MESSAGE.getText());
    }

    @Override
    public String getCommand() {
        return BotCommand.PING.getCommand();
    }
}
