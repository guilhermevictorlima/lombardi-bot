package com.github.guilhermevictorlima.lombardi.api.wrapper.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class LavaPlayerResultHandler implements AudioLoadResultHandler {

    private final ServerMusicManager serverMusicManager;

    public LavaPlayerResultHandler(ServerMusicManager serverMusicManager) {
        this.serverMusicManager = serverMusicManager;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        // TODO usar interaction e avisar da música tocando aqui
        serverMusicManager.getTrackScheduler().queue(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        if (playlist.isSearchResult()) {
            serverMusicManager.getTrackScheduler().queue(playlist.getTracks().get(0));
        } else {
            playlist.getTracks()
                .forEach(this::trackLoaded);
        }
    }

    @Override
    public void noMatches() {
        // TODO avisar que não foi possível encontrar nenhuma faixa
    }

    @Override
    public void loadFailed(FriendlyException e) {
        // TODO avisar que ocorreu um erro ao carregar a faixa ou playlist e logar exception
    }

}
