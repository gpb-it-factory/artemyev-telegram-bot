package com.gpb.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {

    private String botName;
    private String token;

    public BotConfig(@Value("${bot.name}") String botName, @Value("${bot.token}") String token) {
        this.botName = botName;
        this.token = token;
    }
}

