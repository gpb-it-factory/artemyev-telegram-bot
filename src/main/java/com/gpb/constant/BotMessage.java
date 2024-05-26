package com.gpb.constant;

public enum BotMessage {
    WELCOME_MESSAGE("Hello, %s, welcome to the bot!"),
    ANSWER_MESSAGE("pong"),
    DEFAULT_MESSAGE("Sorry, command was not recognized");
    private final String text;

    BotMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
