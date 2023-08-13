package com.github.guilhermevictorlima.lombardi.api.commands;

import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.List;

public abstract class BotCommand {

    abstract protected void execute(SlashCommandInteraction interaction);
    abstract protected String getName();
    abstract protected String getDescription();
    abstract protected List<SlashCommandOption> getOptions();
    abstract protected boolean isSatisfiedBy(SlashCommandInteraction interaction);

    public void buildCommand(DiscordApi api) {
        SlashCommand.with(getName(), getDescription(), getOptions())
            .createGlobal(api)
            .join();
    }

}
