package com.github.guilhermevictorlima.lombardi.api.wrapper.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;

public class AudioPlayerWrapper {

    public void playFromYoutube(String url, AudioConnection audioConnection, DiscordApi api) {
        AudioPlayerManager playerManager = createAudioPlayerManager();
        AudioPlayer player = playerManager.createPlayer();

        AudioSource source = new LavaPlayerAudioSource(api, player);
        audioConnection.setAudioSource(source);

        playerManager.loadItem(url, new LavaPlayerResultHandler(player));
    }

    private AudioPlayerManager createAudioPlayerManager() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());

        return playerManager;
    }

}
