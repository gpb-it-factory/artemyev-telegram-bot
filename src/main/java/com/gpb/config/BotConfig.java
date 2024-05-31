package com.gpb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Data
@ConfigurationProperties(prefix = "bot")
public class BotConfig {

    private final String botName;
    private final String token;

    @ConstructorBinding
    public BotConfig(String botName, String token) {
        this.botName = botName;
        this.token = token;
    }
}

