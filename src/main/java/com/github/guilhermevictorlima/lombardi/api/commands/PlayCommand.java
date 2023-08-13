package com.github.guilhermevictorlima.lombardi.api.commands;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

// TODO remover parâmetros de teste e iniciar implementação do comando
@Service
public class PlayCommand extends BotCommand {

    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.createImmediateResponder()
                .setContent("Teste")
                .respond();
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Toque uma música";
    }

    @Override
    public List<SlashCommandOption> getOptions() {
        return Collections.emptyList();
    }

    @Override
    public boolean isSatisfiedBy(SlashCommandInteraction interaction) {
        return interaction.getCommandName().equals(getName());
    }
}
