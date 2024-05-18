package com.gpb.strategy;

import org.telegram.telegrambots.meta.api.objects.Message;

public final class DefaultCommandStrategy implements CommandProcessingStrategy{
    @Override
    public String process(Message message) {
        return "Sorry, command was not recognized";
    }
}
