package com.gpb.service;

import com.gpb.config.BotConfig;
import com.gpb.constant.BotCommands;
import com.gpb.exception.MessageSendingException;
import com.gpb.handler.CommandStrategyHandler;
import com.gpb.handler.TelegramHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final TelegramHandler telegramHandler;

    public TelegramBot(BotConfig config, TelegramHandler telegramHandler) {
        super(config.getToken());
        this.config = config;
        this.telegramHandler = telegramHandler;
    }

    @PostConstruct
    public void init() {
        initializeCommands();
    }

    private void initializeCommands() {
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand(BotCommands.START.getCommand(), "get a welcome message"));
        botCommands.add(new BotCommand(BotCommands.PING.getCommand(), "get answer pong"));
        botCommands.add(new BotCommand(BotCommands.REGISTER.getCommand(), "register user"));
        botCommands.add(new BotCommand(BotCommands.CREATE_ACCOUNT.getCommand(), "create account"));
        botCommands.add(new BotCommand(BotCommands.CURRENT_BALANCE.getCommand(), "get current balance"));
        botCommands.add(new BotCommand(BotCommands.TRANSFER.getCommand(), "transfer money"));
        try {
            execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot commands" + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            execute(telegramHandler.handleUpdate(update));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Failed to handle update", e);
        }
    }
}
