package model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * model for playing medias.
 */
public class MediaModel {

    /**
     * General MediaPlayer object to work with Media files.
     */
    private static MediaPlayer player;

    /**
     * Variable to check if the mediaplayer is muted or not.
     */
    private static boolean muted;

    /**
     * Variable to check if the mediaplayer has a started playing a song or it is paused.
     */
    private static boolean isPlaying;

    /**
     * Creating a media object from the song param's URI.
     * Plays the media object and sets isPlaying boolean to true.
     *
     * @param song path to song
     */
    public static void setMusic(final String song) {

        Media pick = null;
        try {
            pick = new Media("file:/" + new URI(song));
        } catch (URISyntaxException e) {
            LoggerFactory.getLogger(MediaModel.class).error("Invalid URI");
            e.getStackTrace();
            return;
        }

        if (player != null) {
            player.stop();
        }
        player = new MediaPlayer(pick);

        player.play();

        isPlaying = true;

    }

    /**
     * @return If a media is already been playing in the mediaplayer
     */
    public static boolean isSet() {
        return player != null;
    }

    /**
     * Invoces the mediaplayer's play method to start playing the music.
     */
    public static void playSong() {
        player.play();
    }

    /**
     * Method to change the state of play to pause and back, when Play/Pause button is pressed.
     */
    public static void playPause() {
        if (isPlaying) {
            player.pause();
        } else {
            player.play();
        }
        isPlaying = !isPlaying;
    }

    /**
     * Invoces the mediaplayer's stop method to pause the song and restore it to the begining.
     */
    public static void stopSong() {
        player.stop();
    }

    /**
     * Invoces the mediaplayer's setVolume method to adjust the volume.
     *
     * @param n value to set
     */
    public static void setVolume(final double n) {
        player.setVolume(n);
    }

    /**
     * Invoces the mediaplayer's seek method to adjust progression(time) in the media.
     *
     * @param d playSlider value
     */
    public static void setSeek(final double d) {
        player.seek(new Duration(d * player.getMedia().getDuration().toMillis()));
    }

    /**
     * Method to show the progression(time) in the media file.
     *
     * @return playSlider value
     */
    public static double getSeek() {
        return player.getCurrentTime().toMillis() / player.getMedia().getDuration().toMillis();
    }

    /**
     * Invoce the mediaplayer's setMute method to mute and restore the output volume.
     */
    public static void mute() {
        player.setMute(!muted);
        muted = !muted;
    }
}
