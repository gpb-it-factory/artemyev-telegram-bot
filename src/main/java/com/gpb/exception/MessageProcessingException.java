package com.gpb.exception;

public class MessageProcessingException extends RuntimeException{
    public MessageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
