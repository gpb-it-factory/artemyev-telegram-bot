package com.gpb.strategy;

import com.gpb.constant.BotCommand;
import com.gpb.entity.Response;
import com.gpb.service.RegistrationService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public final class RegisterCommand implements IdentifiableCommand {
    private final RegistrationService registrationService;

    public RegisterCommand(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public SendMessage process(Update update) {
        long chatId = update.getMessage().getChatId();
        Response responseMessage = registrationService.registerUser(chatId);

        return SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(responseMessage.getMessage())
                .build();
    }

    @Override
    public String getCommand() {
        return BotCommand.REGISTER.getCommand();
    }
}
