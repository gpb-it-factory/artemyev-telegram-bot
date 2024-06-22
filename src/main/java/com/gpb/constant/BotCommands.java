package com.gpb.constant;

public enum BotCommands {
    START("/start"),
    PING("/ping"),
    REGISTER("/register"),
    CREATE_ACCOUNT("/createaccount"),
    CURRENT_BALANCE("/current_balance"),
    TRANSFER ("/transfer");

    private final String command;

    BotCommands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
