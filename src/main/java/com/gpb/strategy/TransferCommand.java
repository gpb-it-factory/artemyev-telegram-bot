package com.gpb.strategy;

import com.gpb.cache.UserDataCache;
import com.gpb.constant.BotCommands;
import com.gpb.entity.TransferResponse;
import com.gpb.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.gpb.constant.BotCommands.*;


@Component
@AllArgsConstructor
public class TransferCommand implements IdentifiableCommand {
    private final TransferService transferService;
    private final UserDataCache userDataCache;

    @Override
    public SendMessage process(Update update) {

        Long chatId = update.getMessage().getChatId();
        String userMessage = update.getMessage().getText();
        BotCommands userState = userDataCache.getUserBotState(chatId);

        return switch (userState) {
            case TRANSFER -> handleTransferState(chatId);
            case WAITING_FOR_AMOUNT -> handleWaitingForAmountState(chatId, userMessage);
            case WAITING_CONFIRMATION -> handleWaitingConfirmationState(chatId, userMessage);
            default -> handleDefaultState(chatId);
        };
    }
    private SendMessage handleTransferState(Long chatId) {
        userDataCache.setUserBotState(chatId, WAITING_FOR_AMOUNT);
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Введите имя получателя...")
                .build();
    }

    private SendMessage handleWaitingForAmountState(Long chatId, String userMessage) {
        userDataCache.setRecipientName(chatId, userMessage);
        userDataCache.setUserBotState(chatId, WAITING_CONFIRMATION);
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Введите сумму...")
                .build();
    }

    private SendMessage handleWaitingConfirmationState(Long chatId, String userMessage) {
        userDataCache.setAmount(chatId, userMessage);
        userDataCache.setUserBotState(chatId, START);
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text("Вы действительно хотите выполнить этот перевод?")
                .build();
    }

    private SendMessage handleDefaultState(Long chatId) {
        TransferResponse response = transferService.transfer(chatId, userDataCache.getRecipientName(chatId), userDataCache.getAmount(chatId));
        userDataCache.clearUserData(chatId);
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(response.getMessage())
                .build();
    }

    @Override
    public String getCommand() {
        return BotCommands.TRANSFER.getCommand();
    }
}