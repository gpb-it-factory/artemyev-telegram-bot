package com.gpb.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
public class BotConfig {

    private final String botName;
    private final String token;

    public BotConfig(@Value("${bot.name}") String botName, @Value("${bot.token}") String token) {
        this.botName = botName;
        this.token = token;
    }
}

