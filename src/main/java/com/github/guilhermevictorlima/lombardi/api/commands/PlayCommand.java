package com.github.guilhermevictorlima.lombardi.api.commands;

import com.github.guilhermevictorlima.lombardi.api.wrapper.lavaplayer.AudioPlayerWrapper;
import com.github.guilhermevictorlima.lombardi.exception.CouldNotExecuteCommandException;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PlayCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayCommand.class);

    @Override
    public void execute(SlashCommandInteraction interaction) {
        Optional<ServerVoiceChannel> connectedVoiceChannel = interaction.getUser().getConnectedVoiceChannel(interaction.getServer().get());
        validateVoiceChannel(connectedVoiceChannel, interaction);

        ServerVoiceChannel voiceChannel = connectedVoiceChannel.get();

        voiceChannel.connect()
                .thenAccept(audioConnection -> playSong(interaction, voiceChannel, audioConnection))
                .exceptionally(throwable -> {
                    interaction.createImmediateResponder()
                            .setContent(":x: Xiii deu ruim... Não consegui entrar no canal de voz " + voiceChannel.getName())
                            .respond();

                    throw new CouldNotExecuteCommandException(PlayCommand.class, throwable.toString());
                })
                .join();
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

    private void validateVoiceChannel(Optional<ServerVoiceChannel> connectedVoiceChannel, SlashCommandInteraction interaction) {
        if (connectedVoiceChannel.isEmpty()) {
            interaction.createImmediateResponder()
                    .setContent(":interrobang: **Oh-oh!** Parece que você não está conectado a um canal de voz. Como vou tocar algo assim? Me ajuda a te ajudar né amigão! :man_facepalming:")
                    .respond();

            throw new CouldNotExecuteCommandException(PlayCommand.class, "User is not on a voice channel.");
        }

        ServerVoiceChannel voiceChannel = connectedVoiceChannel.get();
        if (!voiceChannel.canYouConnect()) {
            interaction.createImmediateResponder()
                    .setContent(":x: Xiii deu ruim... Não consigo entrar no canal de voz " + voiceChannel.getName())
                    .respond();

            throw new CouldNotExecuteCommandException(PlayCommand.class, "The bot cannot connect on voice channel.");
        }
    }

    private void playSong(SlashCommandInteraction interaction, ServerVoiceChannel voiceChannel, AudioConnection audioConnection) {
        interaction.createImmediateResponder()
                .setContent(":notes: Tocando https://youtu.be/bMfvZmhqW0A")
                .respond();

        new AudioPlayerWrapper()
                .playFromYoutube("https://youtu.be/bMfvZmhqW0A", audioConnection, interaction.getApi());

    }
}
