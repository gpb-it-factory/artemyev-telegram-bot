package com.gpb.strategy;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandProcessingStrategy {
    String process(Message message);
}
