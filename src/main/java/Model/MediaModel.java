package Model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.net.URI;
import java.net.URISyntaxException;

public class MediaModel {
    private static MediaPlayer player;
    private static boolean muted;
    private static boolean isPlaying;

    public static void setMusic(String song){

        Media pick = null;
        try {
            pick = new Media("file:/" + new URI(song));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if(player != null)
            player.stop();

        player = new MediaPlayer(pick);

        player.play();

        isPlaying = true;

    }
	
	public static boolean isSet(){
		return player != null;
	}
	
    public static void playSong(){
        player.play();
    }

    public static void playPause(){
        if(isPlaying) player.pause();
        else player.play();
        isPlaying = !isPlaying;
    }

    public static void stopSong(){
        player.stop();
    }

    public static void setVolume(double n){
        player.setVolume(n);
    }

    public static void setSeek(double d){
        player.seek(new Duration(d * player.getMedia().getDuration().toMillis()));
    }

    public static double getSeek(){
        return player.getCurrentTime().toMillis() / player.getMedia().getDuration().toMillis();
    }

    public static void mute(){
        player.setMute(!muted);
        muted = !muted;
    }
}
