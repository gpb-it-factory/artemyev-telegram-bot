package com.gpb.strategy;

import com.gpb.constant.BotMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public final class DefaultCommand implements CommandStrategy {

    @Override
    public SendMessage process(Update update) {
        String welcomeText = BotMessage.DEFAULT_MESSAGE.getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(welcomeText);
        return sendMessage;
    }
}
