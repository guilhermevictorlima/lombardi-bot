package com.github.guilhermevictorlima.lombardi.api.wrapper.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class LavaPlayerResultHandler implements AudioLoadResultHandler {

    private final AudioPlayer player;

    public LavaPlayerResultHandler(AudioPlayer player) {
        this.player = player;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        player.playTrack(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        for (AudioTrack track : playlist.getTracks()) {
            player.playTrack(track);
        }
    }

    @Override
    public void noMatches() {
    }

    @Override
    public void loadFailed(FriendlyException e) {

    }

}
