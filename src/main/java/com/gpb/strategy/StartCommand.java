package com.gpb.strategy;

import com.gpb.constant.BotCommand;
import com.gpb.constant.BotMessage;
import com.gpb.exception.MessageSendingException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public final class StartCommand implements IdentifiableCommand {

    @Override
    public SendMessage process(Update update) {
        String welcomeText = String.format(BotMessage.WELCOME_MESSAGE.getText(), update.getMessage().getFrom().getFirstName());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(welcomeText);
        return sendMessage;
    }

    @Override
    public String getCommand() {
        return BotCommand.START.getCommand();
    }


}
