package com.github.guilhermevictorlima.lombardi.api.wrapper.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.github.guilhermevictorlima.lombardi.utils.QuerySearchUtils.getFormattedQuery;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PlayerManager {

    private final Map<Long, ServerMusicManager> serverMusicManagers = new HashMap<>();
    private final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    public PlayerManager() {
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager(true));

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public ServerMusicManager getServerMusicManager(Server server) {
        return serverMusicManagers.computeIfAbsent(server.getId(), (serverId) -> new ServerMusicManager(audioPlayerManager));
    }

    public void play(SlashCommandInteraction interaction, AudioConnection audioConnection) {
        ServerMusicManager serverMusicManager = getServerMusicManager(audioConnection.getServer());
        audioConnection.setAudioSource(new LavaPlayerAudioSource(interaction.getApi(), serverMusicManager.getAudioPlayer()));

        String query = interaction.getOptionByName("QUERY")
                .orElseThrow()
                .getStringValue()
                .orElseThrow();

        audioPlayerManager.loadItemOrdered(serverMusicManager, getFormattedQuery(query), new LavaPlayerResultHandler(serverMusicManager, interaction));
    }

}
