package com.gpb.strategy;

public interface IdentifiableCommandProcessingStrategy extends CommandProcessingStrategy{
    String getCommand();
}
