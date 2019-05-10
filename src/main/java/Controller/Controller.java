package Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;

public class Controller {

    public Button play_button;
    public Button prev_button;
    public Button next_button;
    public Button stop_button;
    public Button mute_button;
    public Button shuffle_button;
    public Button loop_button;

    public Image playImage;
    public Image shuffleImage;
    public Image muteImage;
    public Image loopImage;

    public Slider volumeSlider;
    public Slider play_slider;

    public Controller() {

    }

    @FXML
    private void StartPause() {
        System.out.println("Start");
    }

    @FXML
    private void PrevSong() {
        System.out.println("Start");

    }

    @FXML
    private void NextSong() {
        System.out.println("Start");

    }

    @FXML
    private void StopSong() {
        System.out.println("Start");

    }

    @FXML
    private void MuteDemute() {
        System.out.println("Start");

    }

    @FXML
    private void Shuffle() {
        System.out.println("Start");

    }

    @FXML
    private void Loop() {
        System.out.println("Start");

    }
}