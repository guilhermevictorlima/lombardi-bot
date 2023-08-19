package com.github.guilhermevictorlima.lombardi.api.commands;

import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.List;

public abstract class BotCommand {

    abstract public void execute(SlashCommandInteraction interaction);
    abstract public boolean isSatisfiedBy(SlashCommandInteraction interaction);
    abstract protected String getName();
    abstract protected String getDescription();
    abstract protected List<SlashCommandOption> getOptions();

    public void buildCommand(DiscordApi api) {
        SlashCommand.with(getName(), getDescription(), getOptions())
            .createGlobal(api)
            .join();
    }

}
