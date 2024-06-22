package com.gpb.strategy;

import com.gpb.constant.BotCommands;
import com.gpb.entity.Response;
import com.gpb.service.AccountService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CurrentAccountCommand implements IdentifiableCommand {
    private final AccountService accountService;

    public CurrentAccountCommand(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public SendMessage process(Update update) {
        long chatId = update.getMessage().getChatId();
        Response currentBalance = accountService.getCurrentBalance(chatId);

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(currentBalance.getMessage())
                .build();

    }

    @Override
    public String getCommand() {
        return BotCommands.CURRENT_BALANCE.getCommand();
    }
}
