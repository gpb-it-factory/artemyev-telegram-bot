package com.gpb.strategy;

import org.telegram.telegrambots.meta.api.objects.Message;

public final class PingCommandStrategy implements CommandProcessingStrategy{
    @Override
    public String process(Message message) {
        return "pong";
    }
}
