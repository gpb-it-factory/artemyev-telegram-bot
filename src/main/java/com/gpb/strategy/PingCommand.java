package com.gpb.strategy;

import com.gpb.constant.BotCommand;
import com.gpb.constant.BotMessage;
import com.gpb.exception.MessageSendingException;
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
    public SendMessage process(Message message) throws MessageSendingException {
        try {
            return messageSenderService.sendMessage(message.getChatId(), BotMessage.ANSWER_MESSAGE.getText());
        } catch (Exception e) {
            throw new MessageSendingException("Error sending message to chatId: " + message.getChatId(), e);
        }

    }

    @Override
    public String getCommand() {
        return BotCommand.PING.getCommand();
    }
}
