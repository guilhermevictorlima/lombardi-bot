package com.github.guilhermevictorlima.lombardi.api.wrapper.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.guilhermevictorlima.lombardi.utils.QuerySearchUtils.isUrl;
import static java.lang.String.format;

public class LavaPlayerResultHandler implements AudioLoadResultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LavaPlayerResultHandler.class);

    private final ServerMusicManager serverMusicManager;
    private final SlashCommandInteraction interaction;

    public LavaPlayerResultHandler(ServerMusicManager serverMusicManager, SlashCommandInteraction interaction) {
        this.serverMusicManager = serverMusicManager;
        this.interaction = interaction;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        interaction.createImmediateResponder()
                    .setContent(format("**Tocando** :notes: `%s` - Solta o som!", track.getInfo().title))
                    .respond();

        serverMusicManager.getTrackScheduler().queue(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        if (playlist.isSearchResult()) {
            trackLoaded(playlist.getTracks().get(0));
        } else {
            int playlistSize = playlist.getTracks().size();

            interaction.createImmediateResponder()
                    .setContent(format(":notes: Adicionadas %s faixas da playlist %s ", playlistSize, playlist.getName()))
                    .respond();

            playlist.getTracks()
                .forEach(track -> serverMusicManager.getTrackScheduler().queue(track));
        }
    }

    @Override
    public void noMatches() {
        String query = interaction.getOptionByName("QUERY")
                .orElseThrow()
                .getStringValue()
                .orElseThrow();

        String message = isUrl(query)
                ? ":x: Não consegui encontrar **nada** para tocar nesse link :pensive:"
                : format(":x: Não consegui encontrar **nada** pesquisando por \"%s\" :pensive:", query);

        interaction.createImmediateResponder()
                .setContent(message)
                .respond();
    }

    @Override
    public void loadFailed(FriendlyException e) {
        LOGGER.error(e.toString());

        interaction.createImmediateResponder()
                .setContent(":x: Ocorreu algum problema ao carregar faixa ou playlist")
                .respond();
    }

}
