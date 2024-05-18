package com.gpb.strategy;

import org.telegram.telegrambots.meta.api.objects.Message;

public final class StartCommandStrategy implements CommandProcessingStrategy{
    @Override
    public String process(Message message) {
        return "Hello, " + message.getChat().getFirstName() + ", welcome to the bot!";
    }
}
