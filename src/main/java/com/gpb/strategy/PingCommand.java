package com.gpb.strategy;

import com.gpb.constant.BotCommand;
import com.gpb.constant.BotMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public final class PingCommand implements IdentifiableCommand {
    @Override
    public SendMessage process(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(BotMessage.ANSWER_MESSAGE.getText())
                .build();
    }

    @Override
    public String getCommand() {
        return BotCommand.PING.getCommand();
    }
}
