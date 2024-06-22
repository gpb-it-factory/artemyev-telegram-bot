package com.gpb.cache;

import com.gpb.constant.BotCommands;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserDataCache {
    private final Map<Long, String> userBotState = new ConcurrentHashMap<>();
    private final Map<Long, String> recipientName = new ConcurrentHashMap<>();
    private final Map<Long, String> amount = new ConcurrentHashMap<>();


    public void setUserBotState(long userId, String botCommands) {
        userBotState.put(userId, botCommands);
    }

    public String getUserBotState(long userId) {
        return userBotState.getOrDefault(userId, BotCommands.TRANSFER.getCommand());
    }

    public void setRecipientName(long userId, String name) {
        recipientName.put(userId, name);
    }

    public String getRecipientName(long userId) {
        return recipientName.get(userId);
    }

    public void setAmount(long userId, String amount) {
        this.amount.put(userId, amount);
    }

    public String getAmount(long userId) {
        return amount.get(userId);
    }

    public void clearUserData(long userId) {
        userBotState.remove(userId);
        recipientName.remove(userId);
        amount.remove(userId);
    }
}
