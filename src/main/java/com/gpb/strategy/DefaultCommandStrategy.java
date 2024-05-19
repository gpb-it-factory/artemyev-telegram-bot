package com.gpb.strategy;

import com.gpb.constant.BotMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public final class DefaultCommandStrategy implements CommandProcessingStrategy {
    @Override
    public String process(Message message) {
        return String.format(BotMessage.DEFAULT_MESSAGE.getText());
    }
}
