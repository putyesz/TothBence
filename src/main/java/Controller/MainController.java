package Controller;

import View.AddPathWindow;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class MainController {

    public ImageView playImageView;
    public ImageView shuffleImageView;
    public ImageView muteImageView;
    public ImageView loopImageView;

    public Button playButton;
    public Button prevButton;
    public Button nextButton;
    public Button stopButton;
    public Button muteButton;
    public Button shuffleButton;
    public Button loopButton;

    public Slider volumeSlider;
    public Slider playSlider;

    public MenuItem close_menuitem;
    public MenuItem importMenuitem;
    public MenuItem addPathMenuitem;

    private boolean isPaused;
    private boolean isMuted;
    private boolean isOnShuffle;
    private boolean isLooped;

    private double volumeSliderValue;

    public MainController() {
        init();
    }

    private void init() {
        isPaused = true;
        isMuted = false;
        isOnShuffle = false;
        isLooped = false;

        Import();

/*
        playImageView.setImage(new Image("./Images/playButt.png"));
        muteImageView.setImage(new Image("./Images/muteButt.png"));
        shuffleImageView.setImage(new Image("./Images/shuffle.png"));
        loopImageView.setImage(new Image("./Images/loopButt.png"));
  */  }

    @FXML
    private void StartPause() {
        isPaused = !isPaused;
        if(isPaused) {
            playImageView.setImage(new Image("./Images/playButt.png"));
            System.out.println("Start");
        }
        else {
            playImageView.setImage(new Image("./Images/pauseButt.png"));
            System.out.println("Pause");
        }
    }

    @FXML
    private void PrevSong() {
        System.out.println("Prev");

    }

    @FXML
    private void NextSong() {
        System.out.println("Next");

    }

    @FXML
    private void StopSong() {
        playSlider.adjustValue(0);
        System.out.println("Stop");

    }

    @FXML
    private void MuteUnmute() {
        isMuted = !isMuted;
        if (isMuted){
            muteImageView.setImage(new Image("./Images/unmuteButt.png"));
            volumeSliderValue = volumeSlider.getValue();
            volumeSlider.adjustValue(0);
            System.out.println("Muted");
        }
        else{
            muteImageView.setImage(new Image("./Images/muteButt.png"));
            volumeSlider.adjustValue(volumeSliderValue);
            System.out.println("Unmuted");
        }
    }

    @FXML
    private void Shuffle() {
        isOnShuffle = !isOnShuffle;
        if (isOnShuffle){
            shuffleImageView.setImage(new Image("./Images/shuffleOn.png"));
            System.out.println("On shuffle");
        }
        else{
            shuffleImageView.setImage(new Image("./Images/shuffle.png"));
            System.out.println("Not on shuffle");
        }
    }

    @FXML
    private void Loop() {
        isLooped = !isLooped;
        if (isLooped){
            loopImageView.setImage(new Image("./Images/loopButtOn.png"));
            System.out.println("Looped");
        }
        else{
            loopImageView.setImage(new Image("./Images/loopButt.png"));
            System.out.println("Not looped");
        }
    }

    @FXML
    public void AddPathWindow() throws IOException {
        AddPathWindow iw = new AddPathWindow();
    }

    @FXML
    public void Import(){
    }
}