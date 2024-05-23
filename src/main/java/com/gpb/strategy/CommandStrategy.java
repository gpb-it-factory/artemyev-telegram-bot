package com.gpb.strategy;

import com.gpb.exception.MessageSendingException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandStrategy {
    SendMessage process(Message message) throws MessageSendingException;
}
