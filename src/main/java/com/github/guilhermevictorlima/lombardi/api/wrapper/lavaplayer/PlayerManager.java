package com.github.guilhermevictorlima.lombardi.api.wrapper.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.server.Server;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    public void play(DiscordApi api, AudioConnection audioConnection, String url) {
        ServerMusicManager serverMusicManager = getServerMusicManager(audioConnection.getServer());

        audioConnection.setAudioSource(new LavaPlayerAudioSource(api, serverMusicManager.getAudioPlayer()));

        audioPlayerManager.loadItemOrdered(serverMusicManager, url, new LavaPlayerResultHandler(serverMusicManager));
    }

}
