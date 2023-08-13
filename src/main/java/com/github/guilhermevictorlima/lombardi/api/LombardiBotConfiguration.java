package com.github.guilhermevictorlima.lombardi.api;

import io.github.cdimascio.dotenv.Dotenv;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LombardiBotConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(LombardiBotConfiguration.class);

    private static final String DISCORD_TOKEN_ENV_NAME = "DISCORD_APPLICATION_TOKEN";

    @Bean
    public DiscordApi discordApi() {
        LOGGER.info("Starting Discord API authentication...");

        final String token = Dotenv.configure()
                .load()
                .get(DISCORD_TOKEN_ENV_NAME);

        final DiscordApi discordApi = new DiscordApiBuilder()
                .setToken(token)
                .setIntents(Intent.GUILD_VOICE_STATES)
                .login()
                .join();

        LOGGER.info("Discord API successfully authenticated!");

        return discordApi;
    }

}
