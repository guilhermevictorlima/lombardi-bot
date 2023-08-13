package com.github.guilhermevictorlima.lombardi.api.commands;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.util.List;

public class CommandListener implements SlashCommandCreateListener {

    private final List<BotCommand> commands;

    public CommandListener(List<BotCommand> commands) {
        this.commands = commands;
    }

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();

        commands.stream()
            .filter(command -> command.isSatisfiedBy(interaction))
            .findFirst()
            .ifPresent(command -> command.execute(interaction));
    }

}
