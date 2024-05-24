package com.gpb.strategy;

import com.gpb.exception.MessageSendingException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandStrategy {
    SendMessage process(Update update) throws MessageSendingException;
}
