package com.gpb.strategy;

public interface IdentifiableCommand extends CommandStrategy {
    String getCommand();
}
