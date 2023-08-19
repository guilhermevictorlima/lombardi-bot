package com.github.guilhermevictorlima.lombardi.api.commands;

import com.github.guilhermevictorlima.lombardi.api.wrapper.lavaplayer.PlayerManager;
import com.github.guilhermevictorlima.lombardi.exception.CouldNotExecuteCommandException;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayCommand extends BotCommand {

    private final PlayerManager playerManager;

    public PlayCommand(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        Optional<ServerVoiceChannel> connectedVoiceChannel = interaction.getUser().getConnectedVoiceChannel(interaction.getServer().get());
        validateVoiceChannel(connectedVoiceChannel, interaction);

        ServerVoiceChannel voiceChannel = connectedVoiceChannel.get();

        if (!voiceChannel.isConnected(interaction.getApi().getYourself())) {
            playJoiningChannel(interaction, voiceChannel);
        }

        voiceChannel.getServer().getAudioConnection()
                .ifPresentOrElse(audioConnection -> playSong(interaction, voiceChannel, audioConnection), () -> interaction.createImmediateResponder().setContent("Ocorreu um erro inesperado"));
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
        return List.of(SlashCommandOption.create(SlashCommandOptionType.STRING, "URL", "Link da música no YouTube", true));
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
        if (!voiceChannel.canYouConnect() || !voiceChannel.canYouSee()) {
            interaction.createImmediateResponder()
                    .setContent(":x: Xiii deu ruim... Não consigo entrar no canal de voz " + voiceChannel.getName())
                    .respond();

            throw new CouldNotExecuteCommandException(PlayCommand.class, "The bot cannot connect on voice channel.");
        }

        if (!voiceChannel.hasPermission(interaction.getApi().getYourself(), PermissionType.SPEAK)) {
            interaction.createImmediateResponder()
                    .setContent(":x: Não tenho permissão pra falar no canal de voz " + voiceChannel.getName())
                    .respond();

            throw new CouldNotExecuteCommandException(PlayCommand.class, "The bot is not allowed to speak on the voice channel.");
        }
    }

    private void playSong(SlashCommandInteraction interaction, ServerVoiceChannel voiceChannel, AudioConnection audioConnection) {
        final String url = interaction.getOptionByName("URL")
                .orElseThrow()
                .getStringValue()
                .orElseThrow();

        interaction.createImmediateResponder()
                .setContent(":notes: Tocando " + url)
                .respond();

        playerManager.play(interaction.getApi(), audioConnection, url);
    }

    private void playJoiningChannel(SlashCommandInteraction interaction, ServerVoiceChannel voiceChannel) {
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
}
