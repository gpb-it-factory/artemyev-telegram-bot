package com.gpb.strategy;

import com.gpb.constant.BotCommand;
import com.gpb.constant.BotMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public final class StartCommand implements IdentifiableCommand {

    @Override
    public SendMessage process(Update update) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(String.format(BotMessage.WELCOME_MESSAGE.getText(), update.getMessage().getFrom().getFirstName()))
                .build();
    }

    @Override
    public String getCommand() {
        return BotCommand.START.getCommand();
    }


}
