package com.gpb.constant;

public enum BotCommand {
    START("/start"),
    PING("/ping"),
    REGISTER("/register");

    private final String command;

    BotCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
