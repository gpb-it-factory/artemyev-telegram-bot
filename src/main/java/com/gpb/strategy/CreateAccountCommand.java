package com.gpb.strategy;

import com.gpb.constant.BotCommand;
import com.gpb.entity.Response;
import com.gpb.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class CreateAccountCommand implements IdentifiableCommand {
    private final AccountService accountService;

    public CreateAccountCommand(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public SendMessage process(Update update) {
        long chatId = update.getMessage().getChatId();
        Response response = accountService.createAccount(chatId, "My first awesome account");
        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(response.getMessage())
                .build();
    }

    @Override
    public String getCommand() {
        return BotCommand.CREATE_ACCOUNT.getCommand();
    }
}

