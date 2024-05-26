package com.gpb.exception;

public class MessageSendingException extends Exception{
    public MessageSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
