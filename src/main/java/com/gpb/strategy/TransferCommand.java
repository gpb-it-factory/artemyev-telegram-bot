package com.gpb.strategy;

import com.gpb.cache.UserDataCache;
import com.gpb.constant.BotCommands;
import com.gpb.entity.TransferResponse;
import com.gpb.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@AllArgsConstructor
public class TransferCommand implements IdentifiableCommand {
    private final TransferService transferService;
    private final UserDataCache userDataCache;

    @Override
    public SendMessage process(Update update) {

        Long chatId = update.getMessage().getChatId();
        String userMessage = update.getMessage().getText();
        String userState = userDataCache.getUserBotState(chatId);

        if (userState.equals(BotCommands.TRANSFER.getCommand())) {

            userDataCache.setUserBotState(chatId, BotCommands.WAITING_FOR_AMOUNT.getCommand());

            return SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("Введите имя получателя...")
                    .build();
        } else if (userState.equals(BotCommands.WAITING_FOR_AMOUNT.getCommand())) {

            userDataCache.setRecipientName(chatId, userMessage);
            userDataCache.setUserBotState(chatId, BotCommands.WAITING_CONFIRMATION.getCommand());

            return SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("Введите сумму...")
                    .build();

        } else if (userState.equals(BotCommands.WAITING_CONFIRMATION.getCommand())) {

            userDataCache.setAmount(chatId, userMessage);
            userDataCache.setUserBotState(chatId, BotCommands.START.getCommand());

            return SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("Вы действительно хотите выполнить этот перевод?")
                    .build();

        } else {
            TransferResponse response = transferService.transfer(chatId, userDataCache.getRecipientName(chatId), userDataCache.getAmount(chatId));

            userDataCache.clearUserData(chatId);

            return SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(response.getMessage())
                    .build();
        }

    }

    @Override
    public String getCommand() {
        return BotCommands.TRANSFER.getCommand();
    }
}
