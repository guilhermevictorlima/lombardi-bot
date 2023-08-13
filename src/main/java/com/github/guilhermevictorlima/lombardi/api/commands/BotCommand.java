package com.github.guilhermevictorlima.lombardi.api.commands;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.util.List;

public abstract class BotCommand implements SlashCommandCreateListener {

    abstract protected void execute(SlashCommandInteraction interaction);
    abstract protected String getName();
    abstract protected String getDescription();
    abstract protected List<SlashCommandOption> getOptions();
    abstract protected boolean isSatisfiedBy(SlashCommandInteraction interaction);

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();

        if (isSatisfiedBy(interaction)) {
            execute(interaction);
        }
    }

    public void buildCommand(DiscordApi api) {
        SlashCommand.with(getName(), getDescription(), getOptions())
            .createGlobal(api)
            .join();

        api.addListener(this);
    }

}
