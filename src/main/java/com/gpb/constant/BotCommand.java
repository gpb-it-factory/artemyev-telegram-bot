package com.gpb.constant;

public enum BotCommand {
    START("/start"),
    PING("/ping"),
    REGISTER("/register"),
    CREATE_ACCOUNT("/createAccount"),
    CURRENT_BALANCE("/current_balance");

    private final String command;

    BotCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
