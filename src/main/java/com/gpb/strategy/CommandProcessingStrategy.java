package com.gpb.strategy;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandProcessingStrategy {
    void process(Message message);
}
