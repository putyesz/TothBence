package hu.unideb.inf.Controller;

import Main;
import hu.unideb.inf.Model.Song;
import hu.unideb.inf.View.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import jdk.internal.jline.internal.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class Controller {
    public Button play_button;
    public Button next_button;
    public Button prev_button;
    public Button mute_button;
    public Button shuffle_button;
    public Button loop_button;
    public Slider play_slider;

    /**
     * Logger objektum a logoláshoz
     */
    //Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * EZ itt jó
     */
    private Song model;

    /**
     *
     */
    private View view;

    /**
     * A szám elérési útja
     */
    private String path;

    /**
     * Konstruktor
     *
     * @param model {@link #model}
     * @param view  {@link #view}
     */
    public Controller(Song model, View view) {
        this.model = model;
        this.view = view;

        play_button.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler <MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                StartSong();
            }
        });

        prev_button.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler <MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                PrevSong();
            }
        });

        next_button.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler <MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                NextSong();
            }
        });

        loop_button.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler <MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Loop();
            }
        });

        shuffle_button.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler <MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Shuffle();
            }
        });

        mute_button.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler <MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Mute();
            }
        });
    }

    private void NextSong() {

    }

    private void Loop() {

    }

    private void Shuffle() {

    }

    private void Mute() {

    }

    private void PrevSong() {

    }

    /*public void fc() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*mp3"));
        File file =  fileChooser.showOpenDialog(null);
        path = file.getAbsolutePath();
        path = path.replace("\\", "/");
    }*/

    public void StartSong(){
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.mp3"));
        File file = fc.showOpenDialog(null);
        path = file.getAbsolutePath();
        path = path.replace("\\", "/");

        //kirakni xmlbe

        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaView.setMediaPlayer(mediaPlayer);
    }

    public void Play_Button_Pressed(){

    }

    public void Space_Button_Pressed(KeyEvent keyEvent){
        if (keyEvent.getCode().equals(KeyCode.SPACE)){
            StartSong();
        }
    }
}
