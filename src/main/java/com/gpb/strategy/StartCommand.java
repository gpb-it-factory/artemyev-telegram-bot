package com.gpb.strategy;

import com.gpb.constant.BotCommand;
import com.gpb.constant.BotMessage;
import com.gpb.exception.MessageSendingException;
import com.gpb.service.MessageSenderService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public final class StartCommand implements IdentifiableCommand {
    private final MessageSenderService messageSenderService;

    public StartCommand(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @Override
    public SendMessage process(Message message) throws MessageSendingException {
        String welcomeText = String.format(BotMessage.WELCOME_MESSAGE.getText(), message.getFrom().getFirstName());
        try {
            return messageSenderService.sendMessage(message.getChatId(), welcomeText);
        } catch (Exception e) {
            throw new MessageSendingException("Error sending message to chatId: " + message.getChatId(), e);
        }
    }

    @Override
    public String getCommand() {
        return BotCommand.START.getCommand();
    }


}
