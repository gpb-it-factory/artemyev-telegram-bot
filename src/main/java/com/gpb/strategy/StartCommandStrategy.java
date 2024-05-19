package com.gpb.strategy;

import com.gpb.constant.BotMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public final class StartCommandStrategy implements CommandProcessingStrategy {
    @Override
    public String process(Message message) {
        return String.format(BotMessage.ANSWER_MESSAGE.getText(), message.getChat().getFirstName());
    }
}
