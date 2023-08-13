package com.github.guilhermevictorlima.lombardi.api;

import com.github.guilhermevictorlima.lombardi.api.commands.BotCommand;
import com.github.guilhermevictorlima.lombardi.api.commands.listener.CommandListener;
import io.github.cdimascio.dotenv.Dotenv;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LombardiBotConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(LombardiBotConfiguration.class);

    private static final String DISCORD_TOKEN_ENV_NAME = "DISCORD_APPLICATION_TOKEN";

    @Bean
    public DiscordApi discordApi(List<BotCommand> commands) {
        LOGGER.info("Starting Discord API authentication...");
        final DiscordApi discordApi = authenticateApi();
        LOGGER.info("Discord API successfully authenticated!");

        LOGGER.info("Creating slash commands...");
        commands.forEach(command -> command.buildCommand(discordApi));
        LOGGER.info("Slash commands successfully created!");

        LOGGER.info("Creating command listener...");
        discordApi.addSlashCommandCreateListener(new CommandListener(commands));
        LOGGER.info("Command listener successfully created!");

        return discordApi;
    }

    private DiscordApi authenticateApi() {
        final String token = Dotenv.configure()
                .load()
                .get(DISCORD_TOKEN_ENV_NAME);

        return new DiscordApiBuilder()
                .setToken(token)
                .setIntents(Intent.GUILD_VOICE_STATES)
                .login()
                .join();
    }

}
